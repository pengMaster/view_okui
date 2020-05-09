### 自定义View进阶难点解析
### 应用场景：高级UI绘制 - 镂空

- 1.Paint.ANTI_ALIAS_FLAG 设置抗锯齿
- 2.getWidth()/ getHeight() 需要在布局测量完毕使用 例如onSizeChanged ,如果在初始化方法中使用为0
- 3.Path.Direction.CCW 逆时针 Path.Direction.CW 顺时针
> 从最内部图形发射一条射线穿过所有嵌套图形。
- 4 镂空两种方式
  -  4.1  默认path.fileType = WINDING  （需要看方向）
        - 4.1.1 如果方向相同，全部内部，带有涂色
         -  4.1.2 如果方向相反的穿插次数相等则为内部，不等则为外部
    - 4.2 path.fileType = EVEN_ODD （常用）
         - 4.2.1 不考虑方向。穿插奇数次则为内部，偶数次则为外部
- PathMeasure
    - 把 Path 对象填入，用于对 Path 做针对性的计算（例如图形周长，具体点坐标）
    - pathMeasure.getLength();
    pathMeasure.getPosTan();

### 应用场景：高级UI绘制 - 仪表盘
- 1. [paint.setPathEffect()](https://blog.csdn.net/u010635353/article/details/52701298)
>  可设置画笔效果，画虚线，圆角等，但是会吃掉本次绘制
- 2. PathDashPathEffect
> 刻度中的矩形坐标以当前点为圆心绘制坐标系

### 应用场景：高级UI绘制 - 饼状图

- 1. 个别图形偏移
> 做局部扇形偏移，translate画布，需要注意偏移量用三角函数计算

### 应用场景：高级UI绘制 - 蒙版图形 AvatarView

- 1. setXfermode
> 特别是绘制圆形时，图形周围存在毛边，利用Xfermode做蒙版完美解决
- 2. saveLayer
> 需要设置离屏缓冲将蒙版区域抠出来，==最新API用setLayerType(View.LAYER_TYPE_HARDWARE,null)更加轻量级，不过这个设置针对整个View起作用==


### 应用场景：高级UI绘制 - 文字绘制 SportsView

- 1. 文字上下居中-测量基准 - 4条baseLine基线
>   1.1 方式一:Paint.getTextBounds() 之后，使⽤用 (bounds.top + bounds.bottom) / 2（会造成文字变动上下跳动）
> > 1.2 方式二:Paint.getFontMetrics() 之后，使⽤用 (fontMetrics.ascend + fontMetrics.descend) / 2 （最多文字的中点）
- 2. 文字左对齐
> 多个文字字体不同时会产生左边空隙情况。
> 解决方法：用 getTextBounds() 之后的 left 来计算

### 应用场景：高级UI绘制 - 文字换行 ImageTextView
- 纯文本文字换行可以使用 StaticLayout
- 文字+图片复杂内容用 breakText() 来计算

### Canvas 的范围裁切
==为什么使用xfermode==
- clipRect()
-  clipPath() 切出来的圆为什什么没有抗锯⻮齿效果?因为「==强⾏行行切边==」
- clipOutRect() / clipOutPath()

### Canvas 的几何变换
- translate(x, y)
- rotate(degree)
- scale(x, y)
- skew(x, y)
- 重点:Canvas 的几何变换方法参照的是 ==View 的坐标系==，⽽绘制⽅法(drawXxx())参照的是 ==Canvas ⾃⼰的坐标系==。

### 关于多重变换
Canvas 的变换⽅法多次调⽤的时候，由于 Canvas 的坐标系会整体被变换，因此当平移、旋转、放
缩、错切等变换多重存在的时候，Canvas 的变换参数会⾮常难以计算，因此可以改⽤==倒序==的理解⽅式：
> 将 Canvas 的变换理解为 Canvas 的坐标系不变，每次变换是只对内部的绘制内容进⾏变换，同
> 时把 Canvas 的变换顺序看作是倒序的（即写在下⾯的变换先执⾏），可以更加⽅便进⾏多重变
> 换的参数计算

### Matrix 的⼏何变换
- preTranslate(x, y) / postTranslate(x, y)
- preRotate(degree) / postRotate(degree)
- preScale(x, y) / postScale(x, y)
- preSkew(x, y) / postSkew(x, y)
> 其中 ==preXxx() 效果和 Canvas 的准同名⽅法相同， postXxx() 效果和 Canvas 的准同名⽅法顺序相反。==
#### 注意
如果多次重复使⽤ Matrix，在使⽤之前需要⽤ Matrix.reset() 来把 ==Matrix 重置==。

### 使⽤ Camera 做三维旋转 CameraView
- rotate() / rotateX() / rotateY() / rotateZ()
- translate()
- setLocation()
其中，⼀般只⽤ ==rotateX()== 和 ==rorateY()== 来做沿 x 轴或 y 轴的旋转，以及使⽤
==setLocation()== 来调整放缩的视觉幅度。
对 Camera 变换之后，要⽤ ==Camera.applyToCanvas(Canvas)== 来应⽤到 Canvas。==多次变换也需要反着写，如果两个draw，先进行第一个draw的倒叙，再进行第二个draw的倒叙。==
- 切割在三维变换之前做更加方便

### setLocation()

这个⽅法⼀般前两个参数都填 0，第三个参数为负值。由于这个值的单位是硬编码写死的，因此像素
密度越⾼的⼿机，相当于 Camera 距离 View 越近，所以最好把这个值写成与机器的 density 成正⽐
的⼀个负值，例如 -6 * density
> ==三维图形的适配==：setLocation(x:0,y:0,z:-8) ,-8的单位是英寸，是openGL中sky的东西，1英寸=72像素，使用 ==-8*getResources().getDisplayMetrics().density== 来动态适配。

---

## 属性动画和硬件加速
### 属性动画
#### ViewPropertyAnimator
> 使⽤ View.animate() 创建对象，以及使⽤ ViewPropertyAnimator.translationX() 等⽅法
> 来设置动画；
> 可以连续调⽤来设置多个动画；
> 可以⽤ setDuration() 来设置持续时间；
> 可以⽤ setStartDelay() 来设置开始延时；
> 以及其他⼀些便捷⽅法。

#### ObjectAnimator
> 使⽤ ObjectAnimator.ofXxx() 来创建对象，以及使⽤ ObjectAnimator.start() 来主动启动
> 动画。它的优势在于，可以为⾃定义属性设置动画。
> 另外，⾃定义属性需要设置 getter 和 setter ⽅法，并且 setter ⽅法⾥需要调⽤ invalidate() 来
> 触发重绘：
```
ObjectAnimator animator = ObjectAnimator.ofObject(view, "radius",
Utils.dp2px(200));
public float getRadius() {
 return radius;
}
public void setRadius(float radius) {
 this.radius = radius;
 invalidate();
}
```
> 可以使⽤ setDuration() 来设置持续时间；
> 可以⽤ setStartDelay() 来设置开始延时；
> 以及其他⼀些便捷⽅法。


#### Interpolator

> 插值器，⽤于设置时间完成度到动画完成度的计算公式，直⽩地说即设置动画的速度曲线，通过
> setInterpolator(Interpolator) ⽅法来设置。
> 常⽤的有 AccelerateDecelerateInterpolator（中间加速） AccelerateInterpolator（加速）
> DecelerateInterpolator(减速) LinearInterpolator（匀速）。

#### PropertyValuesHolder

⽤于设置更加详细的动画，例如多个属性应⽤于同⼀个对象：

```
PropertyValuesHolder holder1 = PropertyValuesHolder.ofFloat("radius",
Utils.dp2px(200));
PropertyValuesHolder holder2 = PropertyValuesHolder.ofFloat("offset",
Utils.dp2px(100));
ObjectAnimator animator = PropertyValuesHolder.ofPropertyValuesHolder(view,
holder1, holder2);
```

或者，配合使⽤ Keyframe ，对⼀个属性分多个段：


```
Keyframe keyframe1 = Keyframe.ofFloat(0, Utils.dpToPixel(100));
Keyframe keyframe2 = Keyframe.ofFloat(0.5f, Utils.dpToPixel(250));
Keyframe keyframe3 = Keyframe.ofFloat(1, Utils.dpToPixel(200));
PropertyValuesHolder holder = PropertyValuesHolder.ofKeyframe("radius",
keyframe1, keyframe2, keyframe3);
ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(view,
holder);
```

#### AnimatorSet
将多个 Animator 合并在⼀起使⽤，先后顺序或并列顺序都可以：

```
AnimatorSet animatorSet = new AnimatorSet();
 animatorSet.playTogether(animator1, animator2);
 animatorSet.start();
```
#### TypeEvaluator
⽤于设置动画完成度到属性具体值的计算公式。默认的 ofInt() ofFloat() 已经有了⾃带的
IntEvaluator FloatEvaluator ，但有的时候需要⾃⼰设置 Evaluator。例如，对于颜⾊，需要
为 int 类型的颜⾊设置 ArgbEvaluator，⽽不是让它们使⽤ IntEvaluator

```
animator.setEvaluator(new ArgbEvaluator());：
```
如果你对 ArgbEvaluator 的效果不满意，也可以⾃⼰写⼀个 HsvEvaluator ：

```
public class HsvEvaluator implements TypeEvaluator<Integer> {
 @Override
 public Object evaluate(float fraction, Object startValue, Object
endValue) {
 ...
 }
}
```
另外，对于不⽀持的类型，也可以使⽤ ofObject() 来在创建 Animator 的同时就设置上
Evaluator，⽐如 NameEvaluator ：

```
public class NameEvaluator implements TypeEvaluator<String> {
 List<String> names = ...;
 @Override
 public String evaluate(float fraction, String startValue, String
endValue) {
 if (!names.contains(startValue)) {
 throw new IllegalArgumentException("Start value not existed");
 }
 if (!names.contains(endValue)) {
 throw new IllegalArgumentException("End value not existed");
 }
 int index = (int) ((names.indexOf(endValue) -
names.indexOf(startValue)) * fraction);
 return names.get(index);
 }
}
ObjectAnimator animator = ObjectAnimator.ofObject(view, "name", new
NameEvaluator(), "Jack");
```
#### Listeners
和 View 的点击、⻓按监听器⼀样，Animator 也可以使⽤ setXxxListener()
addXxxListener() 来设置监听器。
#### ValueAnimator
这是最基本的 Animator，它不和具体的某个对象联动，⽽是直接对两个数值进⾏渐变计算。使⽤很
少
### 硬件加速
##### 硬件加速是什么
硬件加速只对系统自带属性，如：translationX，自定义属性不起作用
- 使⽤ CPU 绘制到 Bitmap，然后把 Bitmap 贴到屏幕，就是软件绘制；
- 使⽤ CPU 把绘制内容转换成 GPU 操作，交给 GPU，由 GPU 负责真正的绘制，就叫硬件绘制；
> 开启==硬件加速==：view.setLayerType(View.LAYER_TYPE_HARDWARE,null)
> 开启==软件加速==：view.setLayerType(View.LAYER_TYPE_SOFTWARE,null)
> 关闭：view.setLayerType(View.LAYER_TYPE_NONE,null)

- 使⽤ GPU 绘制就叫做硬件加速

##### 硬件加速的缺陷：
兼容性。由于使⽤ GPU 的绘制（暂时）⽆法完成某些绘制，因此对于⼀些特定的 API，需要关闭硬件
加速来转回到使⽤ CPU 进⾏绘制。
### 离屏缓冲：
- 离屏缓冲是什么：单独的⼀个绘制 View（或 View 的⼀部分）的区域
- setLayerType() 和 saveLayer()
    - setLayerType() 是对整个 View，不能针对 onDraw() ⾥⾯的某⼀具体过程
        - 这个⽅法常⽤来关闭硬件加速，但它的定位和定义都不只是⼀个「硬件加速开关」。
它的作⽤是为绘制设置⼀个离屏缓冲，让后⾯的绘制都单独写在这个离屏缓冲内。如
果参数填写 LAYER_TYPE_SOFTWARE ，会把离屏缓冲设置为⼀个 Bitmap ，即使⽤
软件绘制来进⾏缓冲，这样就导致在设置离屏缓冲的同时，将硬件加速关闭了。但需
要知道，这个⽅法被⽤来关闭硬件加速，只是因为 Android 并没有提供⼀个便捷的⽅
法在 View 级别简单地开关硬件加速⽽已。
    - saveLayer() 是针对 Canvas 的，所以在 onDraw() ⾥可以使⽤ saveLayer() 来圈出具体哪部
分绘制要⽤离屏缓冲
        - 然⽽……最新的⽂档表示这个⽅法太重了，能不⽤就别⽤，尽量⽤ setLayerType() 代
替

---

### 自定义viewGroup TagLayout

#### 方法解析
> 讲义：⾃定义布局
- draw measure 总调度，不实际做事
- onDraw  onMeasure 实际做事
- layout 实际做事，把自己尺寸保存起来
- onLayout 对子View进行布局，调子View layout方法
- setMeasureDimension() 会把新的尺寸存起来，父View以及子View都可以拿到新的尺寸，如果在layout中直接修改自己的尺寸和位置，父View拿不到自己改的尺寸，会出问题，因为layout在最后一步。
- resolveSize() 或者 resolveSizeAndState() 修正结果
>  需要比对 EXACTLY，AT_MOST， UNSPECIFIED。另外resolveSizeAndState中的state 中多传入的值是为了帮助修正，子View的要求父View无法满足时，可以通过state看到偏差，现在基本废弃。
#### viewGroup自定义
- 重写 onMeasure()，遍历每个⼦ View，⽤ measureChildWidthMargins() 测量⼦ View
> 需要重写 generateLayoutParams() 并返回 MarginLayoutParams 才能使⽤
measureChildWithMargins() ⽅法
- measureChildWidthMargins() 的内部实现（最好读⼀下代码，这个极少需要⾃
⼰写，但⾯试时很多时候会考）
> 通过 ==getChildMeasureSpec==(int spec, int padding, int childDimension) ⽅
法计算出⼦ View 的 widthMeasureSpec 和 heightMeasureSpec，然后调
⽤ child.measure() ⽅法来让⼦ View ⾃我测量
- getChildMeasureSpec()
> getChildMeasureSpec(int spec, int padding, int childDimension) ⽅法的内部
实现是，结合开发者设置的 LayoutParams 中的 width 和 height 与⽗ View ⾃
⼰的剩余可⽤空间，综合得出⼦ View 的尺⼨限制，并使⽤
MeasureSpec.makeMeasureSpec(size, mode) 来求得结果



### view的触摸反馈机制
> 讲义：触摸反馈基础
- 事件序列 down开始cancel结束
- getActionMasked()支持多点触控 和 getAction()不支持
- 安卓相比较苹果==卡顿原因之一==：Layout自定义时，默认包含在内的子View按下都是有延时的，只要不是滑动控件，重写==shouldDeleyChildPressedState返回false==可以关闭预按下延时（==100毫秒==）
- Activity(dispatchTouchEvent) -> ViewGroup(dispatchTouchEvent) -> View(dispatchTouchEvent)
> 在==dispatchTouchEvent 中分发interceptTouchEvent 和onTouchEvent==，如果interceptTouchEvent拦截，会通过dispatchTouchEvent再倒叙传递给上级，通知他们结果
- View（OnTouchEvent）-> ViewGroup(OnTouchEvent) -> Activity(OnTouchEvent)
- parent.requestDisallowInterceptTouchEvent() 调用后父View在当前事件序列中就不会拦截子View了
> ViewPager中嵌套ScrollView使用的策略
- GestureDetector 辅助工具

##### view.dispatchTouchEvent
- 如果设置了 OnTouchListener，调⽤OnTouchListener.onTouch()
    - 如果 OnTouchListener 消费了事件，返回 true
    - 如果 OnTouchListener 没有消费事件，继续调⽤⾃⼰的 onTouchEvent()，并返回和
onTouchEvent() 相同的结果
- 如果没有设置 OnTouchListener，同上
viewGroup.dispatchTouchEvent
> 较为复杂，看印象笔记

##### View.onTouchEvent()
> 印象笔记和讲义，重点

### 多点触控
> desc：View中两个列表互不影响的各自滑动
- ==TouchTarget==
    - 作⽤：记录每个⼦ View 是被哪些 pointer（⼿指）按下的
    - 结构：单向链表
- ACTION_DOWN,ACTION_POINTER_DOWN 坐标:p(x,y,index,id),p(x,y,index,id)
    - 用getX()取得坐标是index=0的坐标，并不是哪个手指滑动取哪个
- ==多指index变换，id复用原则==
    - index变换1:两个手指0，1，index为0的手指抬起，index为1的手指idnex会变成0，但是id仍然为1
    - index变换2:三个手指0，1，2，index为1的手指抬起，index为2的手指idnex会变成1，但是id仍然2
    - id复用原则: 当又有新的手指按下，会将id=0重新赋值给新的手指
> index永远是连续的，多指滑动根据id追踪
- 多点触控的三种类型
- - 接⼒型 MultiTouchRelayView
    > 同⼀时刻只有⼀个 pointer 起作⽤，即最新的 pointer。 典型：ListView、
RecyclerView。 实现⽅式：在 ACTION_POINTER_DOWN 和 ACTION_POINTER_UP 时记
录下最新的 pointer，在之后的 ACTION_MOVE 事件中使⽤这个 pointer 来判断位置。
- - 配合型 / 协作型 MultiTouchTogetherView
    > 所有触摸到 View 的 pointer 共同起作⽤。
典型：ScaleGestureDetector，以及 GestureDetector 的 onScroll() ⽅法判断。 实现⽅
式：在每个 DOWN、POINTER_DOWN、POINTER_UP、UP 事件中使⽤所有 pointer 的坐
标来共同更新焦点坐标，并在 MOVE 事件中使⽤所有 pointer 的坐标来判断位置。
- -  例子：MultiTouchTogetherView
        - MotionEvent.ACTION_POINTER_UP://当第二个手指抬起的时候，只有到MOVE的的时候才实际会少一根手指
        - 计算中心点的时候如果是ACTION_POINTER_UP要去除掉这个点
- - 各⾃为战型
    > 各个 pointer 做不同的事，互不影响。 典型：⽀持多画笔的画板应⽤。 实现⽅
式：在每个 DOWN、POINTER_DOWN 事件中记录下每个 pointer 的 id，在 MOVE 事件中
使⽤ id 对它们进⾏跟踪
- - 例子：画板 MultiTouchDrawBoardView
    - 点移动 path.moveTo
    - 添加线 path.lineTo
    - paint.setStrokeJoin 拐角圆滑

### 图片缩放 触摸反馈辅助工具 ScalableImageView
- canvas缩放可以达到内部图形的缩放
- ==GestureDetector==和GestureDetectorCompat比较，带有compat的一般为兼容包，放在support中支持低版本
> 多个回掉方法见讲义
- ==OverScroller==和Scroller区别 ==做惯性滑动用==
    - Scroller滑动速度特别快的时候，图形惯性滑动的仍然特别慢
    - 两者的作用都是做惯性滑动的辅助工具。
    - OverScroller使用填入起始点位置，速度，边界的4个坐标
    - postOnAnimation 每帧都会执行，辅助onfling完成自动动画执行
- ==ScaleGestureDetector== 双指捏撑监听器
    - onScale中getScaleFactor返回两个手指之间的比例

### ViewPager
> 讲义：手写 ViewPager，以及 Android 中的拖拽操作
- scrollTo 反向移动，正值向左边移动
- ==postInvalidateOnAnimation==与postOnAnimation区别
    - 自动调用invalidate
    - 自动回调computeScroll
- getScrollX() 获取划出屏幕View大小
- overScroll.startScroll() 并不会真正移动View，需要自己移动，需要搭配上面两条一起使用
- ==VelocityTracker== 速度计算器，看讲义
- ==与子View确定谁抢占事件条件==
    - onInterceptTouchEvent()确认滑动的⽅式：Math.abs(event.getX() - downX) > ViewConfiguration.getXxxSlop()
    - 告知⼦ View 的⽅式：在 onInterceptTouchEvent() 中返回 true，⼦ View 会收到
ACTION_CANCEL 事件，并且后续事件不再发给⼦ View
    - 告知⽗ View 的⽅式：调⽤ getParent().requestDisallowInterceptTouchEvent(true) 。这
个⽅法会递归通知每⼀级⽗ View，让他们在后续事件中不要再尝试通过
onInterceptTouchEvent() 拦截事件。这个通知仅在当前事件序列有效，即在这组事件结束
后（即⽤户抬⼿后），⽗ View 会⾃动对后续的新事件序列启⽤拦截机制
- ==ViewConfiguration== 取一些手机设置的默认配置，例如：惯性滑动最大，最小速度


---

### 拖拽和嵌套滑动
- OnDragListener 拖动卸载/图片拖进微信
    - API 11 加⼊的⼯具类，⽤于拖拽操作。
    - 使⽤场景：⽤户的「拖起 -> 放下」操作，重在内容的移动。==可以附加拖拽数据==
    - 不需要写⾃定义 View，使⽤ startDrag() / startDragAndDrop() （==可以跨进程==）⼿动开启拖拽
    - ==拖拽的原理==是创造出⼀个图像在屏幕的最上层，⽤户的⼿指拖着图像移动
- ViewDragHelper 策划栏 drawLayout ==DragUpDownLayout==
    - 2015 年的 support v4包中新增的⼯具类，⽤于拖拽操作。
    - 使⽤场景：⽤户拖动 ViewGroup 中的某个⼦ View
    - 需要应⽤在⾃定义 ViewGroup 中调⽤ ViewDragHelper.shouldInterceptTouchEvent() 和
processTouchEvent()，程序会⾃动开启拖拽
    - 拖拽的原理是实时修改被拖拽的⼦ View 的 mLeft, mTop, mRight, mBottom 值
    - ==例子DragUpDownLayout==
        - viewDragHelper.settleCapturedViewAt 是要放置的位置，不是偏移量

### 嵌套滑动
#### 嵌套滑动的场景
- 不同向嵌套
> onInterceptTouchEvent ⽗ View 拦截
> requestDisallowInterceptTouchEvent() ⼦ View 阻⽌⽗ View 拦截
- 同向嵌套
> ⽗ View 会彻底卡住⼦ View
-    - 原因：抢夺条件⼀致，但 ⽗ View 的 onInterceptTouchEvent() 早于⼦ View 的
dispatchTouchEvent()
- 本质上是策略问题：嵌套状态下⽤户⼿指滑动，他是想滑谁？
    - 场景⼀：==NestedScrollView==
        - ⼦ View 能滑动的时候，滑⼦ View；滑不动的时候，滑⽗ View
    -  场景⼆：Google 的样例 ==CoordinatorLayout==
        - ⽗ View 展开的时候：
            - 上滑：优先滑⽗ View
            - 下滑：滑不动（所以可以说还是优先滑⽗ View）
        - ⽗ View 半展开的时候：
            - 向上：优先滑⽗ View，滑到⽗ View 完全收⻬之后开始滑⼦ View
            - 向下：滑⽗ View，滑到⽗ View 完全展开之后开始滑⼦ View
        - ⽗ View 收起的时候：
            - 上滑：滑⼦ View（所以可以说是优先滑⼦ View）
            - 下滑：优先滑⼦ View，滑到⼦ View 顶部的时候开始滑⽗ View
- 解决⽅案：实现策略——⽗ View、⼦ View 谁来消费事件可以实时协商
- 自己实现，子View滑完，父View再滑 ==NestedScrollingChildHelper==
    - 实现NestedScrollingChild2接口，重写方法
    - NestedScrollingChildHelper辅助类接管重写方法的方法
    - Helper.setNestedScrollingEnabled(true);
    - scrollingChildHelper.dispatchNestedScroll(0, 0, 0, unconsumed, null);
> dispatchNestedScroll优先子View消耗，dispatchNestedPreScroll优先父View消耗

---
### 多线程和线程同步
### Java 回收策略
> 没有被 GC Root 直接或间接持有引⽤的对象，会被回收
- ==GC Root==：
    - 1. 运⾏中的线程
    - 2. 静态对象
    - 3. 来⾃ native code 中的引⽤
    > private native void start0(); 操作系统字节码不去操作，需要java虚拟机去操作的方法
- 内部类持有Activity引用

```
    public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        }
        //类1
        class User{
            onCreate();
            String name;
        }
        //类2
        class AsyTask extends AsyncTask {
        onCreate();
        @Override
        protected Object doInBackground(Object[] objects) {
            return null;
        }
        }
    }
```
> User 和 AsyTask都是内部类，都拿到Activity引用，都可以调用Activity任何方法，这个不是内存泄漏的真正原因。

### 多线程的创建方式
- Thread 和 Runnable
- ThreadFactory

    ```
    ThreadFactory factory = new ThreadFactory() {
     int count = 0;
     @Override
     public Thread newThread(Runnable r) {
     count ++;
     return new Thread(r, "Thread-" + count);
     }
    };
    ```
    > 可以统一管理所有线程，比如：名字

- ==Executor== 和线程池
    - newCachedThreadPool()
        - 带缓存，自动创建，缓存，销毁，并且没有上限，一般使用这个
    - newSingleThreadExecutor()
        - 使用一次就结束，场景：取消
    - newFixedThreadPool
        - 固定数量的线程池，批量处理，效率高，并且可统一回收。场景：批量上传/下载图片
    - newScheduledThreadPool 延时执行
> 舒缓型关闭：shutdown(), 禁止新的线程加入等待，执行中的线程执行完毕关闭。
> 强制性关闭：shutdownNow()，立即关闭。

```
new ThreadPoolExecutor(0(起始线程数), Integer.MAX_VALUE（最大线程数）,
                                      60L, TimeUnit.SECONDS,（保存多久不被回收）
                                      new SynchronousQueue<Runnable>()存放线程的工具);
```
- Callable 和 Future

### 疑难杂症
- 主线程本身是个死循环，为什么不会卡顿？
>   主线程是个大循环，怕小循环卡住大循环
- ==避免死锁==？

```
private void setName(){
    synchronized(monitor1){
        name = "";
         synchronized(monitor2){
            x = "";
        }
    }
}

private void setX(){
    synchronized(monitor2){
        x = "";
         synchronized(monitor1){
            name = "";
        }
    }
}
```
> setName拿到monitor1等待拿monitor2，setX拿到monitor2等待拿monitor1，这样两个谁都不释放锁，形成死锁。
- 乐观锁，悲观锁？
> 悲观锁就是现在这种，乐观锁是先不加锁，改完之后发现值和拿的时候不一样再加锁改一次。

### 线程同步与线程安全
- synchronized  保护的是资源
> synchronized  保护的是资源不是方法以及代码块
例如：private synchronized void setName(),private synchronized void setId(),当setName被锁住时，setId也无法访问（==同一个Monitor监视==），如果想他们互不影响，可以改为多个Monitor
- ==volatile==
    - 保证加了 volatile 关键字的字段的操作具有原⼦性和同步性，其中原⼦性相当于实现了针对
单⼀字段的线程间互斥访问。因此 volatile 可以看做是简化版的 synchronized。
    - volatile 只对基本类型 (byte、char、short、int、long、float、double、boolean) 的赋值
操作和对象的引⽤赋值操作有效。
> a=0;有效，a=a+1，无效；user = new User()可以，user.name = ""无效；
- ==Atomic==Integer AtomicBoolean 等类，作⽤和 volatile 基本⼀致，可以看做是
通⽤版的 volatile
> integer.incrementAndGet()可以看作a=a+1，可以保证a=a+1的安全
- Lock /  ReentrantLock
> 和synchronized原理一样，但是更加麻烦，需要在finally中lock.unlock()释放锁
- ==效率==
> synchronized 会避开CPU缓存，读内存到CPU，写完再考进去)，会很慢，默认我们的数据操作都会在内存拷贝一份数据值到CPU缓存进行计算，效率比较高
- 读写锁 .readLock .writeLock

```
    ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    Lock readLock = lock.readLock();
    Lock writeLock = lock.writeLock();
    private int x = 0;
    private void count() {
     writeLock.lock();
     try {
     x++;
     } finally {
     writeLock.unlock();
     }
    }
    private void print(int time) {
     readLock.lock();
     try {
     for (int i = 0; i < time; i++) {
     System.out.print(x + " ");
     }
     System.out.println();
     } finally {
     readLock.unlock();
     }
    }
```
> 读写不影响，读的读，写的写
- 线程安全问题的本质
>在多个线程访问共同的资源时，在某⼀个线程对资源进⾏写操作的中途（写⼊已经开始，但还没
结束），其他线程对这个写了⼀半的资源进⾏了读操作，或者基于这个写了⼀半的资源进⾏了写
操作，导致出现数据错误。

### 线程间交互
- ⼀个线程终结另⼀个线程
    - Thread.stop() 强制停止，废弃
    - ==Thread.interrupt()==：温和式终结：不⽴即、不强制
        - interrupted() 和 isInterrupted()：检查（和重置）中断状态，==需要中断的地方自己判断然后return==
            -  Thread.interrupt()中断后会重置状态位
            -  isInterrupted 不会
        - InterruptedException 和 ==Thread.sleep为什么加try catch==

        ```
         {
             if(Thread.Interrupt()){
                 //收尾
                 return;
             }
             try{
                // 一些耗时操作
                 Thread.sleep(2000);
             }catch(InterruptedException e){
                  //收尾
                 return;
             }
         }

        ```
        >  如果外部调用thread.interrupt 执行InterruptedException的耗时操作会抛出异常，并且==重置打断状态位==。因为急需推出这个线程，所以interrupt会跳过被InterruptedException包裹的代码
- ==Object.wait()== 和 Object.notify() / notifyAll()
    - 在未达到⽬标时 wait()
    - ⽤ while 循环检查
    - 设置完成后 notifyAll()
    - wait() 和 notify() / notifyAll() 都需要放在同步代码块⾥

    ```
    private synchronized void initString(){
        string = "12345";
        notifyAll();
    }
    private synchronized void printString(){
    while(null == string){
        try{
            wait();
        }catch(InterruptedException e){

        }
            System.out.print(string);
        }
    }
    ```
    > 如果printString在initString之前执行，会拿不到值，所以==wait会先释放掉自己的锁==，去等待结果，当修改完值后调用notifyAll（），再去执行wait后面的代码.为什么在object中，因为要是同一个monitor
- Thread.==join==()：让另⼀个线程插在⾃⼰前⾯
- Thread.==yield==()：暂时让出⾃⼰的时间⽚给同优先级的线程

### Android 的 Handler 机制
- [ActivityThread](https://www.cnblogs.com/mingfeng002/p/10323668.html)
- ==Handler==：负责任务的定制和线程间传递
- ==MessageQueue== 存放Message消息的链表，Message中存放Handler，Runnable，传递的消息等。
- ==Looper== 负责循环、条件判断和任务执⾏
    - ThreadLocal<String> threadLocal 每个线程内操作自己的值，线程之间不共享,类似于map.put(threadId,value)
    - looper.prepare 会把当前线程设置到looper中，threadLocal.set(new Looper())
    - ==looper.loop== 开始死循环，判断MessageQuene中是否有message，方法中会调用message.target.dispatchMessage(),message.target是Handler，Handler.dispatchMessage判断如果有runnable，执行它，如果没有，执行handleMessage
- HandlerThread 具体的线程，使用场景少
- AsyncTask
    - AsyncTask 的内存泄露，其他类型的线程⽅案（Thread、Executor、HandlerThread）⼀样都有，所以不要忽略它们，或者认为 AsyncTask ⽐别的⽅
案更危险。并没有。
    - 就算是使⽤ AsyncTask，只要任务的时间不⻓（例如 10 秒之内），那就完全没
必要做防⽌内存泄露的处理。
- Service 和 IntentService
    - Service：后台任务的活动空间。适⽤场景：⾳乐播放器等。
    - ==IntentService==：执⾏单个任务后⾃动关闭的 Service，包含有==Context==的异步任务，场景：闹钟

### 组件化、插件化和热更新
### 需要用到知识点
- javac Test.java 可以将java文件编译成Test.class
- java Test 可以执行Test.class文件
- build tools 中d8可以将 .class 编译成.dex
- JVM通过ClassLoader读取class文件，然后把字节码转换成平台/系统能够使用的机器码，因此实现跨平台

- 反射

```
try {
//获取类
 Class utilClass = Class.forName("com.hencoder.demo.hidden.Util");
 //获取构造方法
 Constructor utilConstructor = utilClass.getDeclaredConstructors()[0];
 utilConstructor.setAccessible(true);//开启访问权限
 //创建对象
 Object util = utilConstructor.newInstance();
 //创建方法
 Method shoutMethod = utilClass.getDeclaredMethod("shout");
 shoutMethod.setAccessible(true);//开启访问权限
 //调用方法
 shoutMethod.invoke(util);
} catch (ClassNotFoundException e) {
 e.printStackTrace();
} catch (NoSuchMethodException e) {
 e.printStackTrace();
} catch (IllegalAccessException e) {
 e.printStackTrace();
} catch (InstantiationException e) {
 e.printStackTrace();
} catch (InvocationTargetException e) {
 e.printStackTrace();
}
```
### 组件化
> 拆成多个 module 开发就是组件化

### 插件化
> App 的部分功能模块在打包时并不以传统⽅式打包进 apk ⽂件中，⽽是以另⼀种形式⼆次封装进 apk
内部，或者放在⽹络上适时下载，在需要的时候动态对这些功能模块进⾏加载，称之为插件化。
- ==插件化原理：动态加载==
    - 通过⾃定义 ClassLoader 来加载新的 dex ⽂件，从⽽让程序员原本没有的类可以被使⽤，这就是插件
化的原理。
- 代码实现
> 拿到Apk，用DexClassLoader拿到里面的class，在用反射调方法
```
DexClassLoader classLoader = new DexClassLoader(f.getPath(),
getCodeCacheDir().getPath(), null, null);
Class oldClass =
classLoader.loadClass("com.hencoder.demo.hidden.Util");
//接反射代码
```
- ==如何启动 Activity==
    - 解决⽅式⼀：代理 Activity
        - 宿主项目中写一个PreRealActivity，注册好，里面用反射获取到插件Apk中的ShellActivity（不是真正Activity，只是一个class就可以），然后把ShellActivity的所有方法相对应的放到PreRealActivity的生命周期中执行
    - 解决⽅式⼆：欺骗系统
        - 在系统读取到要启动Activity 的指令后换掉Activity，目前==流行方案==
    - 解决⽅式三：重写 gradle 打包过程，合并 AndroiManifest.xml
        - 不合适
- ==资源⽂件⽆法加载==
    - 解决⽅式：⾃定义 AssetManager 和 Resources 对象

```
    private AssetManager createAssetManager(String dexPath) {
     try {
     AssetManager assetManager = AssetManager.class.newInstance();
     Method addAssetPath =
    assetManager.getClass().getMethod("addAssetPath", String.class);
     addAssetPath.invoke(assetManager, dexPath);
     return assetManager;
     } catch (Exception e) {
     e.printStackTrace();
     return null;
     }
    }

    private Resources createResources(AssetManager assetManager) {
     Resources superRes = mContext.getResources();
     Resources resources = new Resources(assetManager,
    superRes.getDisplayMetrics(), superRes.getConfiguration());
     return resources;
    }
```
- ==Android App Bundles==
    - 属于「模块化发布」。未来也许会⽀持动态部署，但肯定会需要结
合应⽤商店（即 Play Store，或者未来被更多的商店所⽀持，那样插件化就彻底没有存在意义了）
#### 关于 DEX：
- class：java 编译后的⽂件，每个类对应⼀个 class ⽂件
- dex：Dalvik EXecutable 把 class 打包在⼀起，⼀个 ==dex 可以包含多个 class ⽂件==
- ==odex==：Optimized DEX ==针对系统的优==化，例如某个⽅法的调⽤指令，会把虚拟的调⽤转换为使
⽤具体的 index，这样在执⾏的时候就不⽤再查找了
- oat：Optimized Android file Type。使⽤ AOT 策略对 dex 预先编译（解释）成本地指令，这样
再运⾏阶段就不需再经历⼀次解释过程，程序的运⾏可以更快
    - ==AOT==：Ahead-Of-Time compilation ==预先编译==

### 热修复
- ==ClassLoader==
    - 工作原理：==双亲委托==
    > 从下面向上递归findLoadedClass查找父类中的缓存是否有这个class，没找到在从上到下调用方法findClass自己加载
![avatar](https://github.com/pengMaster/picApplyGit/blob/master/okUi/WechatIMG4.jpeg)

```
protected Class<?> loadClass(String name, boolean resolve)
        throws ClassNotFoundException
    {
            // First, check if the class has already been loaded
            //缓存中找
            Class<?> c = findLoadedClass(name);
            if (c == null) {
                try {
                    if (parent != null) {
                    //向父类传递
                        c = parent.loadClass(name, false);
                    } else {
                        c = findBootstrapClassOrNull(name);
                    }
                } catch (ClassNotFoundException e) {
                    // ClassNotFoundException thrown if class not found
                    // from the non-null parent class loader
                }

                if (c == null) {
                    // If still not found, then invoke findClass in order
                    // to find the class.
                    //自己加载
                    c = findClass(name);
                }
            }
            return c;
    }
```
- ==findClass==
> BaseDexClassLoader的findClass -> PathList的findClass -> Element的findClass(调用dexFile.loadClassBinaryName native方法)
替换PathList中的elements就可以达到热更新
- ==操作步骤==
- - 1.获取PathList中老得的elements

```
                ClassLoader classLoader = getClassLoader();
                Class loaderClass = BaseDexClassLoader.class;
                Field pathListField = loaderClass.getDeclaredField("pathList");
                pathListField.setAccessible(true);
                Object pathListObject = pathListField.get(classLoader);
                Class pathListClass = pathListObject.getClass();
                Field dexElementsField = pathListClass.getDeclaredField("dexElements");
                dexElementsField.setAccessible(true);
                Object dexElementsObject = dexElementsField.get(pathListObject);
```
-    - 2.获取新的elements

```
                PathClassLoader newClassLoader = new PathClassLoader(apk.getPath(), null);
                Object newPathListObject = pathListField.get(newClassLoader);
                Object newDexElementsObject = dexElementsField.get(newPathListObject);
```
- - 3.将新的dex插到老得dex前面

```
                int oldLength = Array.getLength(dexElementsObject);
                int newLength = Array.getLength(newDexElementsObject);
                Object concatDexElementsObject = Array.newInstance(dexElementsObject.getClass().getComponentType(), oldLength + newLength);
                for (int i = 0; i < newLength; i++) {
                    Array.set(concatDexElementsObject, i, Array.get(newDexElementsObject, i));
                }
                for (int i = 0; i < oldLength; i++) {
                    Array.set(concatDexElementsObject, newLength + i, Array.get(dexElementsObject, i));
                }

                dexElementsField.set(pathListObject, concatDexElementsObject);
```

- ==自动生成dex==

```
task hotfix {
    doLast {
        exec {
            commandLine 'rm', '-r', './build/patch'
        }
        exec {
            commandLine 'mkdir', './build/patch'
        }
        exec {
            commandLine 'javac', "./src/main/java/${patchPath}.java", '-d', './build/patch'
        }
        exec {
            commandLine '/Users/pengmaster/.android-sdk/build-tools/28.0.3/d8', "./build/patch/${patchPath}.class", '--output', './build/patch'
        }
        exec {
            commandLine 'mv', "./build/patch/classes.dex", './build/patch/hotfix.dex'
        }
    }
```
> 命令行执行 .gradlew hotfix 即可

---
### HTTP
- REST HTTP
- get delete 幂等
- post put 不是幂等
- ==head== 和 GET 唯⼀区别在于，返回的==响应中没有 Body== 可以用于大文件下载，先获取他的大小，再分块传输，断点续传，继续传输返回状态码100
- Location==重定向 http转https== 状态码返回301
- HOST不是用来寻址：为什么输入域名，因为主机可能对应多个域名服务。 ，DNS寻址：拿着域名问域名系统对应ip，返回多个ip或者一个
- 端口是TCP端口 HTTP没有端口
- ==content-length== 内容长度 如果是二进制数据，无法定义什么时候算==结束==，所以服务器按照content-length的长度定义结束
- ==content-type== 几种方式 见讲义
    - multitype/form-data
    >  boundary=----
WebKitFormBoundary7MA4YWxkTrZu0gW 内容分隔符
- ==单文件传输 applacation/zip  image/jepg== 更高效，用multitype/form-data 有点浪费
- ==Transfer-Encoding: chunked== 分块传输编码
    - 服务器需要耗时，先给客户端一部分数据，再慢慢给
- ==Range / Accept-Range 断点续传、多线程下载==
    - Accept-Range: bytes 响应报⽂中出现，表示服务器⽀持按字节来取范围数据
    - Header中添加 Range: bytes=<start>-<end> 请求报⽂中出现，表示要取哪段数据
    - Content-Range:<start>-<end>/total 响应报⽂中出现，表示发送的是哪段数据
- ==Cache/buffer==
    - buffer 缓冲 针对流， 提前生产一部分存起来供你消费。视频播放提前加载的视频流
    - cache 缓存 用过的东西等会可能会复用， 在客户端或中间⽹络节点缓存数据，降低从服务器取数据的频率，以提⾼⽹络性能
        - ==cache-control==:no-cache 可以缓存，客户端访问资源需要询问是否过期。
no-store 没有缓存，max-age：可以缓存，在指定时间内直接访问，不用问我。
        -  ==last-modified== 文件最后修改时间。
        -  Etag 指纹，客户端拿自己的和服务器比对是否过期
        -  ==cache-control：private/public==  告诉路上的中间节点是否需要缓存，public缓存，private不缓存


---
### 登录授权
#### 登录和授权的区别
授权：由身份或==持有的令牌==确认享有某些权限（例如获取⽤户信息）。登录过程实质上的⽬的也
是为了确认权限
#### HTTP 中确认授权（或登录）的两种⽅式
##### 1. 通过 Cookie
- 会话管理：登录状态、购物⻋ header中添加sessionId
- 个性化：⽤户偏好、主题
- Tracking：分析⽤户⾏为
##### 危险：
- XSS (Cross-site scripting)
> 即使⽤ JavaScript 拿到浏览器的
Cookie 之后，发送到⾃⼰的⽹站.
==应对⽅式：set-cookie:sessionId = 1;HttpOnly==
这样JavaScript就拿不到了
- XSRF (Cross-site request forgery)
> 你登录着银行网站没有过期，然后你来我网站我给你转接到银行并进行给我转账。
==应对⽅式：Referer 校验。==

##### 2. 通过 ==Authorization Header==
##### 2.1 ==Basic==：
Authorization: Basic <username:password(Base64ed)>
>Authorization: Basic uwehfiushdihu=
将用户名密码Base64之后发送出去
##### 2.2 ==Bearer==：
Authorization: Bearer <bearer token>
- bearer token 的获取⽅式：通过 ==OAuth2 的授权流程==
> 网站请求Github申请第三方授权 -> github返回Authorization code -> 网站把Authorization code发给自己的服务器 -> 服务器将 Authorization code 和⾃⼰的 client secret发送给Github获取access token。==一般access token不会给客户端，否则这个流程没有意义，Authorization code只是说可以给你，不包含敏感信息。==
- 在⾃家 App 中使⽤ Bearer token
>有的 App 会在 Api 的设计中，将登录和授权设计成类似 OAuth2 的过程，但简化掉
Authorization code 概念。即：登录接⼝请求成功时，会返回 access token，然后客户端在之
后的请求中，就可以使⽤这个 access token 来当做 bearer token 进⾏⽤户操作了。
- Refresh token
> access token 有失效时间，在它失效后，调⽤ refresh token 接⼝，传⼊ refresh_token
来获取新的 access token
---
### TCP / IP 协议族
#### 为什么要分层？
客户端到服务器中间有很多节点，文件又大，老是断需要续传，所以把文件切成块，分块传输，所以需要多层配合去完成。
#### 具体分层：
- Application Layer 应⽤层：HTTP、FTP、DNS
    > HTTP、FTP都需要分块，所以把分块干活的人抽出来，他自己本身不负责分块，只负责把任务下发。
- Transport Layer 传输层：TCP、UDP
    - UDP 网断了不需要重传，比如吃鸡游戏，卡了恢复后更新到最新的位置就好
    - TCP负责给信息分块编号，负责确认哪些信息没有传到。都传完后组装好通知http，消息传完了
- Internet Layer ⽹络层：IP
    > 负责具体每块的传输，只管传输
- Link Layer 数据链路层：以太⽹、Wi-Fi

#### ==TCP 连接==
- TCP建立
 ==三次握手==
    - 1.客户端向服务端说：我要向你发消息了
    - 2.服务端向客户端说：第一：我知道了，第二：我也要向你发消息了。期间发送的消息都是TCP消息
    - 3.客户端向服务端说：我也知道了
- TCP关闭
==四次挥手==
    - 1.客户端向服务器说：我要关闭了
    - 2.服务端向客户端说：我知道了
    - 3.服务端向客户端说：我也要关闭了
    - 4.客户端向服务端说：我知道了
> 与建立链接相比，第二阶段拆成了两个，因为服务端关闭之前可能还有未发送完的消息，所以等发完了再主动向客户端说：我也要关闭了

#### ⻓连接
==⼼跳==。即在⼀定间隔时间内，使⽤ TCP 连接发送超短⽆意义消息来让⽹关不能将⾃⼰定义为「空闲连
接」，从⽽防⽌⽹关将⾃⼰的连接关闭。
---

### ==HTTPS==
HTTP over SSL 的简称，即⼯作在 SSL （或 TLS）上的 HTTP。说⽩了就是加密通信的 HTTP
#### ⼯作原理
在客户端和服务器之间协商出⼀套对称密钥，每次发送信息之前将内容加密，收到之后解密，达到内
容的加密传输
#### 为什么不直接⽤⾮对称加密？
⾮对称加密由于使⽤了复杂了数学原理，因此计算相当复杂，如果完全使⽤⾮对称加密来加密通信内
容，会严重影响⽹络通信的性能
#### ==HTTPS 连接建⽴的过程==
1. 客户端向服务端发送：Client Hello
> 还会附带自己支持的版本：TLS版本，Cipher Suite（对称加密算法，非对称解密算法，hash算法），随机数（客户端/服务端两个）
2. Server Hello
> 服务器会把客户端发的选定套装版本，发送回客户端
3. 服务器证书 信任建⽴
> 服务器发过来证书和相关信息
4. Pre-master Secret
> 唯一非对称机密过程，非对称加密Pre-master Secret一个随机数发送给服务器。然后==双方利用三个随机数（包含上面1，2步骤中的随机数）生成Master Secret,在用Master Secret双方都生成（客户端加密密钥，服务端加密密钥，客户端MAC Secret，服务端MAC Secret）==
5. 客户端通知：将使⽤加密通信
> 实际会把上面所有信息都包起来发送过去，下面几步也是这样操作
6. 客户端发送：Finished
7. 服务器通知：将使⽤加密通信
8. 服务器发送：Finished

#### ==服务器证书==
>上面过程3，服务器发过来的证书包含什么内容，证书信任证书机构，证书机构信任证书签发方，证书签发方在手机或者电脑里存着
- 服务器地址
- 证书公钥
- 证书签名
    - 证书机构公钥
    - 证书机构信息
        - 证书签发机构方

#### 需要⾃⼰写证书验证过程的场景
- ⽤的是⾃签名证书（例如只⽤于内⽹的 https）
- 证书信息不全，缺乏中间证书机构（可能性不⼤）
- ⼿机操作系统较旧，没有安装最新加⼊的根证书
---
### 编码、加密、Hash
#### 古典密码学
- 移位式加密
> 如密码棒，使⽤布条缠绕在⽊棒上的⽅式来对书信进⾏加密
- 替换式加密
> 按规则使⽤不同的⽂字来替换掉原先的⽂字来进⾏加密
#### 现代密码学
- 可以加密任何⼆进制数据 (现代可以加密图片/视频)
- ⾮对称加密的出现使得密码学有了更⼴泛的⽤途：数字签名

#### 对称加密
> 使⽤加密算法配合上密钥来加密，解密时使⽤加密过程的完全逆过程配合
密钥来进⾏解密,类似于 ==替换式加密==
- 经典算法
==DES==（56 位密钥，密钥太短⽽逐渐被弃⽤）、==AES==（128 位、192 位、256 位密钥，现在最流⾏）
- 缺点
    - ==密钥无法发送==
    - 容易被破解

#### ⾮对称加密
> 使⽤公钥对数据进⾏加密得到密⽂；使⽤私钥对数据进⾏加密得到原数据
- ==可以发送公钥==
>A单位有公钥A和私钥A。想和谁通信把公钥发给谁，让对方拿自己的公钥加密，然后发回来，自己拿私钥解密。
- 公钥可以根据私钥算出来，所以传输公钥而不是私钥。比特币的椭圆曲线算法

#### 签名与验证
- 私钥加密，对方拿自己的公钥解密。因为只有自己有私钥，所以没有人能造出这样的数据来，因此能验证数据来源于我
- ==防止别人伪造数据==
- 经典算法：==RSA==（可⽤于加密和签名）、DSA（仅⽤于签名，但速度更快，椭圆曲线 ECDSA）
---
### Base64
#### 什么是==⼆进制数据==？
- ⼴义：所有计算机数据都是⼆进制数据
- ሀ义：==⾮⽂本数据即⼆进制数据==

#### 算法
将原数据每 6 位对应成 Base 64 索引表中的⼀个字符编排成⼀个字符串（每个字符 8 位）。
#### Base64 的缺点
因为⾃身的原理（6 位变 8 位），因此每次 Base64 编码之后，==数据都会增⼤约 1/3==，所以会影响存
储和传输性能。
#### 「Base64 加密图⽚传输更安全和⾼效」？？？
==不==。⾸先，Base64 并不是加密；另外，Base64 会导致数据增⼤ 1/3，降低⽹络性能，增⼤⽤户流量
开销，是画蛇添⾜的⼿段。
#### Base58
⽐特币使⽤的编码⽅式，去掉了 Base64 中的数字 "0"，字⺟⼤写 "O"，字⺟⼤写 "I"，和字⺟⼩写
"l"，以及 "+" 和 "/" 符号，⽤于⽐特币地址的表示， 对于「==⼈⼯抄写==」更加⽅便，另外，去掉了 "+" "/" 号后也让⼤多数的软件可以==⽅便双击选==
---
### 压缩与解压缩
#### 常⻅压缩算法
DEFLATE（.zip使用的是该算法）、JPEG、MP3 等
#### ==压缩是编码吗？==
是。所谓编码，即把数据从⼀种形式转换为另⼀种形式。压缩过程属于编码过程，解压缩过程属于解
码过程
---
### 序列化
把数据对象（⼀般是内存中的，例如 JVM 中的对象）转换成字节序列的过程。对象在程序内存⾥的存
放形式是==散乱的（存放在不同的内存区域、并且由引⽤进⾏连接），通过序列化可以把内存中的对象
转换成⼀个字节序列==，从⽽使⽤ byte[] 等形式进⾏本地存储或⽹络传输，在需要的时候重新组装（反
序列化）来使⽤。
#### ⽬的
==让内存中的对象可以被储存和传输==。
#### 序列化是编码吗？
###### 不是
---
### Hash
相当于从==数据中提出摘要信息==，因此最主要⽤途是数字指纹
#### 经典算法
==MD5 SHA1 SHA256==
#### 实际⽤途
- ==数据完整性验证==
    - 从⽹络上下载⽂件后，通过⽐对⽂件的 Hash 值（例如 MD5、SHA1），可以确认下载的⽂件是否有
损坏。如果下载的⽂件 Hash 值和⽂件提供⽅给出的 Hash 值⼀致，则证明==下载的⽂件是完好⽆损==
的。
- ==快速查找==
    - HashMap
        - key 的hash值 在内存地址中比对是否该位置已经存放数据，如果没有则存上。如果对象不重写hashCode却使用HashMap，会造成数据混乱。
    - 对象重写hashCode 直接比对hashCode结果就可以确定是否为同一对象
- 隐私保护
    - 登录密码做hash，为了防止被破解，可以用加盐的方式，就是MD5（密码+333）进行hash，333为盐。

#### Hash 是编码吗？
不是。 ==Hash 是单向过程==，往往是不可逆的，⽆法进⾏逆向恢复操作，因此 Hash 不属于编码。==编码是将数据改变形式，还能改变回来的，可逆的。==

#### Hash 是加密吗？
不是。Hash 是单向过程，⽆法进⾏逆向回复操作，因此 Hash 不属于加密。（记住，==MD5 不是加
密==！），
---
### ==MVC==
#### Android中默认项目就MVC
> 主流理解，MVC变种
- 布局就是View层
- Activity就是Control层
- 网络下载数据就是Model层

```
public class MainActivity extends AppCompatActivity {
    EditText data1View;
    EditText data2View;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        data1View = findViewById(R.id.data1View);
        data2View = findViewById(R.id.data2View);

        String[] data = DataCenter.getData();
        data1View.setText(data[0]);
        data2View.setText(data[1]);
    }
}
```
### ==变种MVC==
- View抽出来为View层
- Activity仍然是Control层
- 网络下载就是model层
> 在默认基础上把View抽出来，面向接口，将DateView实例化成IView接口
```
public class DataView extends LinearLayout implements MvcActivity.IView {
    EditText data1View;
    EditText data2View;

    public DataView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        data1View = findViewById(R.id.data1View);
        data2View = findViewById(R.id.data2View);
    }

    @Override
    public void showData(String data1, String data2) {
        data1View.setText(data1);
        data2View.setText(data2);
    }
}
```

```
<com.hencoder.a25_mvc_mvp_mvvm.mvc.DataView xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/dataView"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    tools:context=".MainActivity">

    <EditText
        android:id="@+id/data1View"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />

    <EditText
        android:id="@+id/data2View"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />

    <Button
        android:id="@+id/saveButton"
        android:layout_width="100dp"
        android:layout_height="wrap_content"
        android:text="Save"/>

</com.hencoder.a25_mvc_mvp_mvvm.mvc.DataView>
```

```
public class MvcActivity extends AppCompatActivity {
    IView dataView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mvc);

        dataView = findViewById(R.id.dataView);

        String[] data = DataCenter.getData();
        dataView.showData(data[0], data[1]);
    }

    interface IView {
        void showData(String data1, String data2);
    }
}

```

### ==主流MVP==
> 思路：把逻辑从Activity拆出来搞成Present，然后把本身传入Activity的更新View方式替换成用接口。
- 网络下载数据为Model层
- Activity为View层
- 逻辑处理为Present层
> 如果不面向接口IView，那么就需要把Activity传到Present层中，面向接口扩展性高，可以把他俩一起考虑理解。
```
public class Presenter {
    IView iView;

    Presenter(IView mvpActivity) {
        this.iView = mvpActivity;
    }

    void load() {
        String[] data = DataCenter.getData();
        iView.showData(data[0], data[1]);
    }

    interface IView {
        void showData(String data1, String data2);
    }
}

```

```
public class MvpActivity extends AppCompatActivity implements Presenter.IView {
    EditText data1View;
    EditText data2View;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        data1View = findViewById(R.id.data1View);
        data2View = findViewById(R.id.data2View);

        new Presenter(this).load();
    }

    @Override
    public void showData(String data1, String data2) {
        data1View.setText(data1);
        data2View.setText(data2);
    }
}
```

### 理解
> 变种==MVC 和 MVP== 其实都是View和Model隔离，严格意义上没有优略势，并且没有特别明显的界限。他俩都是架构规范，灵活运用。

### ==MVVM==
> 一个库，按照他实现。可以理解为实现双向绑定的MVP

#### 数据
1.外部数据。网络或者数据库数据
2.内存数据。方法中定义的值。
3.表现数据。界面上展示的数据。

#### ==双向绑定==
就是将表现数据和内存数据绑定，自动改变。框架可以将表现数据绑定外部数据。


```
//M层：
public class MvvmActivity extends AppCompatActivity {
    EditText data1View;
    EditText data2View;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        data1View = findViewById(R.id.data1View);
        data2View = findViewById(R.id.data2View);

        new ViewModel(new ViewBinder(), data1View, data2View).load();
    }
}
```

```
//V层
public class ViewBinder {
    void bind(final EditText editText, final ViewModel.TextAttr text) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.equals(text.getText())) {
                    text.setText(s.toString());
                }
            }
        });
        text.setOnChangeListener(new ViewModel.TextAttr.OnChangeListener() {
            @Override
            public void onChange(String newText) {
                if (!newText.equals(editText.getText().toString())) {
                    editText.setText(newText);
                }
                System.out.println("被动改变: " + newText);
            }
        });
    }
}

```

```
//V层
public class ViewModel {
    TextAttr data1 = new TextAttr();
    TextAttr data2 = new TextAttr();

    ViewModel(ViewBinder binder, EditText editText1, EditText editText2) {
        binder.bind(editText1, data1);
        binder.bind(editText2, data2);
    }

    void load() {
        String[] data = DataCenter.getData();
        data1.setText(data[0]);
        data2.setText(data[1]);
    }

    static class TextAttr {
        private String text;
        private OnChangeListener listener;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
            if (listener != null) {
                listener.onChange(text);
            }
        }

        void setOnChangeListener(OnChangeListener listener) {
            this.listener = listener;
        }

        interface OnChangeListener {
            void onChange(String newText);
        }
    }
}

```

```
//Model层
public class DataCenter {
    public static String[] getData() {
        return new String[] {"Hi", "Author"};
    }

    public static void setData() {

    }
}

```
---
### 反射注解
- Annotation
    - 特殊Interface
    ```
    //运行范围 什么时期有效 RUNTIME 一直有效
    //SOURCE 只有自己能看到 CLASS 能运行在class中但是虚拟机未必能看到
    @Retention(RetentionPolicy.RUNTIME)
    //作用范围 字段/对象等
    @Target(ElementType.FIELD)
    public @interface BindView {
        int value();
        //int id() default 1;可以设置多个值
    }

    //使用的地方
    @BindView(R.id.view,id = 2)
    ```
    - 反射方法

    ```
    public static void bind(Activity activity){
        for (Field field : activity.getClass().getDeclaredFields()) {
            try {
                BindView bindView = field.getAnnotation(BindView.class);
                if (null != bindView) {
                    field.setAccessible(true);
                    field.set(activity,activity.findViewById(bindView.value()));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    ```
- Dagger 依赖注入，butternife不是依赖注入
> Activity依赖内部的字段。

```
class User{
    String name;
    int id;
    User(String name,int id){
        this.name = name;
        this.id = id;
    }
}

MainActivity中：
User user = new User("name",1);
通过外部将值赋值的方式叫做依赖注入
```

### AnnotationProcessor
> 性能高于反射
- annotationProcessor(':lib_processor') 编译期有效，不会增大包体积。因此要把==processor和BindView分开==
- 1.创建BindView

```
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.FIELD)
public @interface BindView {
    int value();
}

```
- 2.新建BindProcessor，自动生成MainActivityBinding类，其中包含该Activity的findViewById方法
> annotationProcessor project(':28_lib_processor') 引入主项目
```
public class BindingProcessor extends AbstractProcessor {
    Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        filer = processingEnv.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getRootElements()) {
            String packageStr = element.getEnclosingElement().toString();
            String classStr = element.getSimpleName().toString();
            ClassName className = ClassName.get(packageStr, classStr + "$Binding");
            MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder()
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(ClassName.get(packageStr, classStr), "activity");
            boolean hasBinding = false;

            for (Element enclosedElement : element.getEnclosedElements()) {
                BindView bindView = enclosedElement.getAnnotation(BindView.class);
                if (bindView != null) {
                    hasBinding = true;
                    constructorBuilder.addStatement("activity.$N = activity.findViewById($L)",
                            enclosedElement.getSimpleName(), bindView.value());
                }
            }

            TypeSpec builtClass = TypeSpec.classBuilder(className)
                    .addModifiers(Modifier.PUBLIC)
                    .addMethod(constructorBuilder.build())
                    .build();

            if (hasBinding) {
                try {
                //将生成的类写入Build文件中
                    JavaFile.builder(packageStr, builtClass)
                            .build().writeTo(filer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    // 声明要检查的文件类型
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(BindView.class.getCanonicalName());
    }
}
```
- 3.在使用findViewById之前，通过反射初始化MainActivityBinding

```
            // new MainActivityBinding(activity);
            Class bindingClass = Class.forName(activity.getClass().getCanonicalName() + "$Binding");
            Class activityClass = Class.forName(activity.getClass().getCanonicalName());
            Constructor constructor = bindingClass.getDeclaredConstructor(activityClass);
            constructor.newInstance(activity);
```
---
### Gradle
- ==自动化脚本==
- 目录结构分解

```
//对根目录文件配置
buildscript {
    repositories {
        //classpath 'com.android.tools.build:gradle:3.5.3' 的下载地址
        google()
        jcenter()
    }
    dependencies {
        //指定项目中要用哪些plugin apply plugin: 'com.android.application'
        classpath 'com.android.tools.build:gradle:3.5.3'
        //等价于
        //add('classpath','com.android.tools.build:gradle:3.5.3')
    }
}

//对每个project的配置
allprojects {
    repositories {
        google()
        jcenter()

    }
}
//等价于
//allprojects (new Action<Project>() {
//    @Override
//    void execute(Project project) {
//        repositories {
//            google()
//            jcenter()
//
//        }
//    }
//})

```

#### gradle 是什么
- 是构建⼯具，不是语⾔
 - 它⽤了 Groovy 这个语⾔，创造了⼀种 DSL，但它本身不是语⾔
- 闭包
    -  相当于可以被传递的代码块

```
allprojects {
    repositories {
        google()
        jcenter()

    }
}
或者
allprojects （{
    repositories （{
        google()
        jcenter()

    }）
}）
```
#### ==buildType 版本控制==
> src下面创建相应internal目录，目录下放相应操作代码 ，编译的时候会打在一起，main中可直接调用，使用Build Varians工具切换版本
```
    buildTypes {
        //内测版
        internal{
            //延用debug的配置
            initWith debug
        }
        //正式版
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
```
#### ==productFlavors 渠道包==
> 会和debug internal release 进行累加

```
    //渠道包 收费版/免费版
    flavorDimensions 'price'
    productFlavors{
        free{
            dimension 'price'
        }
        paid{
            dimension 'price'
        }
    }
```
#### compile, implementation 和 api ==加快编译过程==
- implementation：不会传递依赖
- compile / api：会传递依赖；api 是 compile 的替代品，效果完全等同
- 当依赖被传递时，⼆级依赖的改动会导致 0 级项⽬重新编译；当依赖不传递时，⼆级依赖的改动
不会导致 0 级项⽬重新编译

####  ==gradle如何确定项⽬结构==
    - 单 project：build.gradle
    - 多 project：由 settings.gradle 配置多个
    - ==查找 settings 的顺序==：
        - 1. 当前⽬录
        - 2. 兄弟⽬录 master
        - 3. ⽗⽬录
#### ==doFirst() doLast()== 和普通代码段的区别：
- 普通代码段：在 task 创建过程中就会被执⾏，发⽣在 configuration配置阶段
- doFirst() 和 doLast()：在 task 执⾏过程中被执⾏，发⽣在 execution执行 阶段。如果⽤户没有
直接或间接执⾏ task，那么它的 doLast() doFirst() 代码不会被执⾏
- doFirst() 和 doLast() 都是 task 代码，其中 doFirst() 是往队列的前⾯插⼊代码，doLast()
是往队列的后⾯插⼊代码
- task 的依赖：可以使⽤ task ==taskA(dependsOn: b)== 的形式来指定依赖。指定依赖后，==b在taskA之前执行==。
    ```
    //普通代码 configuration阶段执行
    println('before')
    task clean(type: Delete) {
        //普通代码configuration阶段执行
        println('before')
        //任务代码 execution阶段执行
        delete rootProject.buildDir
        //普通代码configuration阶段执行
        println('after')
    }
    ```
> 可以用于==读取配置文件==中的信息,==自动提交脚本==

#### gradle 执⾏的==⽣命周期==
- ==三个阶段==：
    - 1.初始化阶段：执⾏ settings.gradle，确定主 project 和⼦ project
    - 2.定义阶段：执⾏每个 project 的 bulid.gradle，确定出所有 task 所组成的有向⽆环图
    - 3.执⾏阶段：按照上⼀阶段所确定出的有向⽆环图来执⾏指定的 task
- 在阶段之间插⼊代码：
    - ⼀⼆阶段之间：
settings.gradle 的最后
    - ⼆三阶段之间：
```
        //最外层的build.gralde
        //afterEvaluate是在有向无环图画完之后执行
        afterEvaluate {
                 插⼊代码
                }
```
### Gradle Plugin
- Groovy
> 单引号是不带转义的，⽽双引号内的内容可以使⽤ "string1${var}string2"的⽅式来转义

#### Gradle Plugin
- 本质：把逻辑独⽴的代码抽取和封装

##### Plugin 的最基本写法
> 写在 build.gradle ⾥

```
class PluginDemo implements Plugin<Project> {
 @Override
 void apply(Project target) {
 println 'Hello author!'
 }
}
apply plugin: PluginDemo
```
##### ==Plugin 通用做法==
- 与app同级，新建模块buildSrc
- main下面新建resources/groovy两个目录
- resources/META-INF/gradle-plugins/*.properties 中的 * 是插件的名称，例如
*.properties 是 plugin.properties ，最终在应⽤插件是的代码就
应该是：

```
apply plugin: 'plugin'
```
- *.properties 中只有⼀⾏，格式是：

```
implementation-class=com.plugin_demo.DemoPlugin
```
- groovy下面新建 PluginDemo.groovy

```
public class pluginDemo implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        //name是在build.gradle中可以用的配置名称
        def extension = project.extensions.create("name",Extension)
        //project.afterEvaluate 等待有向无环图枸建完成再执行，否则无值
        project.afterEvaluate {
            println(" hello ${extension.name}")
        }
    }
}
```
- groovy下面新建 Extension
.groovy

```
public class Extension {
    String name = "";
}
```
- build.gradle中

```
apply plugin: 'plugin'

name{
    name 'mingzi'
}

```
