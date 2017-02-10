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

### 2 EnvironmentLoader
EnvironmentLoader是一个简单的Java类，负责在web容器启动时创建WebEnvironment对象，并存放在ServletContext中。  
WebEnvironment对象包含WebSecurityManager对象。  
#### 2.1 EnvironmentLoaderListener
EnvironmentLoaderListener是EnvironmentLoader的扩展，核心功能仍由EnvironmentLoader提供，但实现了ServletContextListener接口，能够在Web容器启动或关闭时创建或销毁WebEnvironment对象。

### 3 Filter
Shiro web的验证基于Filter实现。ShiroFilter拦截所有请求，是Shiro验证机制的入口。  
### 3.1 AbstractFilter
AbstractFilter是Filter的简单实现，是Shiro 所有Filter的基类。仅保存了FilterConfig对象并提供了获取初始参数的方法getInitParam(), 具体的逻辑需要子类实现onFilterConfigSet()抽象方法。此外doFilter方法也需要子类实现。
#### 3.1.1 NameableFilter
NameableFilter为Filter命名，如果没有显示指定，则默认使用在web.xml中配置的名称。

##### 3.1.1.1 OncePerRequestFilter
OncePerRequestFilter 实现了doFilter()方法，用于处理每一次请求，并按照规则分发到Shiro特定的Filter中，具体需要子类实现doFilterInternal()方法。  

###### 3.1.1.1.1 AbstractShiroFilter
AbstractShiroFilter 基本实现了所有Shiro Filter的标准行为，子类仅需要实现特定的配置逻辑，重写init()方法即可。  

####### 3.1.1.1.1.1 ShiroFilter
最基本的、可用的Shiro Filter。  
需要配置在web.xml中，拦截所有请求。  
此外，ShiroFilter需要依赖WebEnvironment的配置信息，所以必须在web.xml中配置EnvironmentLoaderListener，在容器启动时创建WebEnvironment对象。  
![ShiroFilterHierarchy](resources/images/ShiroFilterHierarchy.png)  

## Tips
Shiro内部对工厂模式的使用案例：
![ShiroFactory](resources/images/ShiroFactory.png)  
充分运用Java特性，首先抽象出一个工厂接口Factory<T>，继而实现统一的工厂方法，层层递进，一步步重用公共代码，下放具体接口，直到具体实现。代码层次清晰，便于扩展。  


~ 未完，待续 ~