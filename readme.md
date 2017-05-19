# React Native Timer Native

Fork of the package [react-native-timer](https://github.com/fractaltech/react-native-timer) adds support of native android time difference between the device and the remote debugger when in JS Remote Debug mode

### Install

`npm install --save react-native-timer-native`

#### Android


 - Add the following line to the bottom of your project's `settings.gradle` file.

    `project(':react-native-timer-native').projectDir = new File(settingsDir, '../node_modules/react-native-timer-native/android')`

 - add an `include` line in your project's `settings.gradle` to include the `:react-native-timer-native` project.

    `include ':react-native-timer-native'`

 - Open your app's `build.gradle` file and add the following line to the `dependencies` block.

    `compile project(":react-native-timer-native")`

 - In your app's `MainActivity.java` file, add `new TimerReactPackage()` to the return statement of the `getPackages()` function.

```
...
    new MainReactPackage(),
    new TimerReactPackage()
...
```

 - Then in the same file add the import statement :
 `import com.odinvt.reactnativetimernative.TimerReactPackage;`

#### A better way to manage timers in react-native with ES6 components, using __WeakMap__.


1. Often you need to do things like show a message for a few seconds, and then hide it, or run an operation again and again at a specific interval. These things will usually happen *inside* a React Component, and will start *after* a component has mounted. So, you really cannot *just* do a `setTimeout(fn, 2000)` for non trivial things. You need to do a `this.timer = setTimeout(fn, 2000)`, and then `clearTimeout(this.timer)` in `componentWillUnmount`.

2. When a component unmounts, these timers have to be cleared and, so that you are not left with zombie timers doing things when you did not expect them to be there.

3. React, right now, offers a solution using the `react-native-timer-mixin` for this. However, mixins are not part of ES6-7 standard, and probably will never be as they get in the way of good software design. And this brings us to the package in question, `react-native-timer-native`.

4. With `react-native-timer-native`, you can set different timers, like `timeout`, `interval` etc in the context of a react component, and unmount all of them when the component unmounts, at context level.

Generic API:

(Automatically makes up for the difference of time when in remote js debug mode)

```js
const timer = require('react-native-timer-native');

// timers maintained in the Map timer.timeouts
timer.setTimeout(name, fn, interval);
timer.clearTimeout(name);
timer.timeoutExists(name);

// timers maintained in the Map timer.intervals
timer.setInterval(name, fn, interval);
timer.clearInterval(name);
timers.intervalExists(name);

// timers maintained in the Map timer.immediates
timer.setImmediate(name, fn);
timer.clearImmediate(name);
timers.immediateExists(name);

// timers maintained in the Map timer.animationFrames
timer.requestAnimationFrame(name, fn);
timer.cancelAnimationFrame(name);
timers.animationFrameExists(name);

```

Mostly, using timers is a pain *inside* react-native components, so we present to you
__Contextual Timers__. API:
```js

timer.setTimeout(context, name, fn, interval);
timer.clearTimeout(context, name);
timer.clearTimeout(context) // clears all timeouts for a context
timer.timeoutExists(context, name);

timer.setInterval(context, name, fn, interval);
timer.clearInterval(context, name);
timer.clearInterval(context); // clears all intervals for a context
timer.intervalExists(context, name);

timer.setImmediate(context, name, fn);
timer.clearImmediate(context, name);
timer.clearImmediate(context); // clears all immediates for a context
timer.immediateExists(context, name);

timer.requestAnimationFrame(context, name, fn);
timer.cancelAnimationFrame(context, name);
timer.cancelAnimationFrame(context); // cancels all animation frames for a context
timer.animationFrameExists(context, name);


```

Example Below:

```js
const timer = require('react-native-timer-native');

class Foo extends React.Component {
  state = {
    showMsg: false
  };

  componentWillUnmount() {
    timer.clearTimeout(this);
  }

  showMsg() {
    this.setState({showMsg: true}, () => timer.setTimeout(
      this, 'hideMsg', () => this.setState({showMsg: false}), 2000
    ));
  }

  render() {
    return {
      <View style={{flex: 1}}>
        <TouchableOpacity onPress={() => requestAnimationFrame(() => this.showMsg())}>
          <Text>Press Me</Text>
        </TouchableOpacity>

        {this.state.showMsg ? (
          <Text>Hello!!</Text>
        ) : (
          null
        )}
      </View>
    }
  }
}


```

PS: Kinda not a best practice, but `const t = require('react-native-timer-native')` can cut down some typing.
Also, this lib can be used in browsers too, but will focus on them when I am working with them.
