# RocketTransfer

##仿茄子快传:
* 在局域网内进行文件（发送方的app、图片等文件，项目中只是进行了手机中的第三方app和手机图片的）的传输。
* 如果没有接收方建立热点，发送方接入热点，然后进行文件的传输。
* 添加android与pc之间的文件传输，通过在android设备中实现http server来完成（这样不需要在pc中安装应用，只需要浏览器就可以方便完成操作）



## 实现原理：

* 设备发现：通过`UDP`向255.255.255.255发送广播包
* 文件传输：通过NIO socket。
* android下httpServer的实现通过apache的httpcore。

# 效果图:

需要两部手机，连接在同一个wifi环境下，会相互发现，并且通过点击发现后的对方，建立连接，进而进行文件的传输。

![image](https://raw.githubusercontent.com/way1989/RocketTransfer/master/filetransfer.gif "效果图")

