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