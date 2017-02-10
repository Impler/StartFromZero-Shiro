# Shiro Web

## 基本构件
### 1 Environment 
Environment对象是Shiro功能的基础，一般由解析配置文件生成。其包含了所有Shiro所需的功能性组件。
#### 1.1 NamedObjectEnvironment
NamedObjectEnvironment提供通过name查找Shiro组件对象的机制。
##### 1.1.1 DefaultEnvironment
Environment接口的简单实现，提供基于key-value形式的对象存储。key为对象名，value为对象本身。
#### 1.2 WebEnvironment
Web环境特定下的Environment，新增访问FilterChainResolver、ServletContext、WebSecurityManager的API（getter/读操作）。
##### 1.2.1 MutableWebEnvironment
在WebEnvironment的基础上增加设置FilterChainResolver、ServletContext、WebSecurityManager的API（setter/写操作）
###### 1.2.1.1 DefaultWebEnvironment
默认WebEnvironment的实现。
####### 1.2.1.1.1 ResourceBasedWebEnvironment
通过配置文件路径初始化WebEnvironment。
######## 1.2.1.1.1.1 IniWebEnvironment
基于Ini格式配置文件初始化的WebEnvironment。  

![EnvironmentHierarchy](resources/images/EnvironmentHierarchy.png)

### EnvironmentLoader
EnvironmentLoader是一个简单的Java类，负责在web容器启动时创建WebEnvironment对象，并存放在ServletContext中。  
WebEnvironment对象包含WebSecurityManager对象。  
#### EnvironmentLoaderListener
EnvironmentLoaderListener是EnvironmentLoader的扩展，核心功能仍由EnvironmentLoader提供，但实现了ServletContextListener接口，能够在Web容器启动或关闭时创建或销毁WebEnvironment对象。

## Tips
Shiro内部对工厂模式的使用案例：
![ShiroFactory](resources/images/ShiroFactory.png)  
充分运用Java特性，首先抽象出一个工厂接口Factory<T>，继而实现统一的工厂方法，层层递进，一步步重用公共代码，下放具体接口，直到具体实现。代码层次清晰，便于扩展。  

~ 未完，待续 ~