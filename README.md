# RadarView
### 仿王者荣耀战力分析图
---
![战力分析图](https://github.com/fengfeilong0529/RadarView/blob/master/pics/2.png "feilong")

![效果图](https://github.com/fengfeilong0529/RadarView/blob/master/pics/1.png "feilong")

# demo查看
![下载二维码](https://github.com/fengfeilong0529/RadarView/blob/master/pics/demo.png "feilong")

本库仅做学习使用

# Usage

Step 1. Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.fengfeilong0529:RadarView:v1.0'
	}
  
Step 3. Add it in xml

```
<com.feilong.radarviewlib.RadarView
        android:id="@+id/radarView"
        android:layout_width="340dp"
        android:layout_height="200dp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:outTextSize="14sp"/>
```
