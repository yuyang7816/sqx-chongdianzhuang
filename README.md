<div align="center">

# **省钱兄 JAVA 充电桩全套开源系统（JAVA+Uni-app+Vue）**

**服务端 · 管理端 · 用户端**

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE)
[![JDK](https://img.shields.io/badge/JDK-1.8-orange.svg)](docs/服务端-运行教程.md)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.1.8-6DB33F.svg)](docs/服务端-运行教程.md)
[![Vue](https://img.shields.io/badge/Vue-2.5-42b883.svg)](docs/管理端-运行教程.md)
[![uni-app](https://img.shields.io/badge/uni--app-用户端-2b9939.svg)](docs/用户端-运行教程.md)

八年沉淀，分批次开源由 AI 辅助开发的系统源码。  
开源为生态，服务才是根基。欢迎各位开发者一同交流。

</div>

---
⭐ 如果觉得项目不错，请点击右上角 Star 收藏支持！您的 Star 是我们持续开源迭代的最大动力！

## **1. 项目简介**

**省钱兄充电桩全套开源系统**（码上充电桩）是一套面向无人自助充电业务的运营系统，覆盖找站、扫码充电、订单结算、钱包充值、门店加盟与后台运营，采用前后端分离架构，服务端、管理端、用户端全套代码开源。

服务端基于 **Spring Boot 2.1.8**（**Java 8**）构建，负责接口、数据库，以及支付、短信、地图、对象存储等外部能力。  
管理端基于 **Vue 2 + Element UI** 开发，供运营人员在电脑浏览器中使用。  
用户端基于 **uni-app** 开发，覆盖手机网页和微信小程序，应用名称是「充电桩」。

本仓库为服务端、管理端、用户端全套源码。

- **官方网站开源计划**：[https://www.xiansqx.com](https://www.xiansqx.com)
- **开源计划**：[https://opensource.xiansqx.cn](https://opensource.xiansqx.cn)
- **官方文档**：[功能说明与运行教程](docs/服务端-运行教程.md)
- **Gitee 仓库**：[https://gitee.com/maxiaodong1996/sqx-chongdianzhuang.git](https://gitee.com/maxiaodong1996/sqx-chongdianzhuang.git)
- **GitHub 仓库**：[https://github.com/yuyang7816/sqx-chongdianzhuang.git](https://github.com/yuyang7816/sqx-chongdianzhuang.git)

## **2. 用户端能力概览**

底部三个主入口：**首页、订单、我的**。

| 模块 | 能做什么 |
|---|---|
| 首页 | 附近充电站 / 门店、城市切换、站点详情、收费标准、扫一扫开充、确认支付 |
| 订单 | 充电订单列表与详情、续单 / 退款相关状态查看 |
| 我的 | 资料与登录、我的车辆、钱包充值与明细、优惠券、会员、积分、邀请好友、帮助与设置 |
| 商家中心 | 门店数据、充电位管理、商家订单、时长统计、提现与优惠券发放（商家账号） |

## **3. 开源说明**

> **【省钱兄科技｜开源计划启动】**
>
> 八年沉淀，我们将分批次开源全套由 AI 辅助开发的系统源码。  
> 本项目采用 **Apache 2.0** 许可协议，源码供学习、技术研究、商业使用、二次开发与参考使用。  
> **开源为生态，服务才是根基；** 欢迎各位开发者一同交流。

## **4. 快速开始**

建议顺序：**先服务端，再管理端，最后用户端。** 管理端和用户端都要等服务端已经启动，才能登录和加载数据。

### **4.1 服务端**

安装 JDK 8、Maven、MySQL 8。可选安装 Redis（当前配置默认关闭）。导入 [`cdz_server/db/chongdianzhuang.sql`](cdz_server/db/chongdianzhuang.sql)（演示库，全表结构 + 菜单/配置/门店样例），在 `cdz_server/src/main/resources/application-prod.yml` 中改成你本机的库名、账号和密码，再在 `cdz_server` 目录启动服务端。  
详细步骤见 [服务端运行教程](docs/服务端-运行教程.md)。

默认端口 **8174**，接口前缀 `/sqx_fast`。接口文档可在启动后打开：

http://127.0.0.1:8174/sqx_fast/swagger-ui.html

### **4.2 管理端**

安装 Node.js 18（`package.json` engines 要求 `>=18 <19`），在 `cdz_admin` 里把代理指向本机服务端后执行安装并启动。  
详细步骤见 [管理端运行教程](docs/管理端-运行教程.md)。

### **4.3 用户端**

用 HBuilderX 打开 `cdz_uniapp`，把 `common/config.js` 改成本机服务端地址后，运行到浏览器或微信开发者工具。  
详细步骤见 [用户端运行教程](docs/用户端-运行教程.md)。

后台演示账号一般为：**admin** / **123456**（以导入库中数据为准）。  
地图、短信、微信、支付宝、对象存储等密钥需要自行申请。申请入口写在 [用户端功能说明](docs/用户端-功能说明.md) 末尾。

## **5. 文档**

| 想做的事 | 打开哪一份 |
|---|---|
| 把服务端跑起来 | [服务端运行教程](docs/服务端-运行教程.md) |
| 看服务端能做什么 | [服务端功能说明](docs/服务端-功能说明.md) |
| 把管理端跑起来 | [管理端运行教程](docs/管理端-运行教程.md) |
| 看管理端能做什么 | [管理端功能说明](docs/管理端-功能说明.md) |
| 把用户端跑起来 | [用户端运行教程](docs/用户端-运行教程.md) |
| 看用户端能做什么 | [用户端功能说明](docs/用户端-功能说明.md) |

## **6. 目录结构**

仓库根目录放三端代码、说明和协议。编译出来的 `target`、`dist`、`unpackage`、`node_modules` 是运行时生成的，不需要改。

### **6.1 仓库根目录**

| 路径 | 是什么 |
|---|---|
| `cdz_server` | **服务端**。接口、支付、短信、文件上传、充电站与订单等能力都在这里；演示库在 `cdz_server/db/chongdianzhuang.sql` |
| `cdz_admin` | **管理端**。运营人员在电脑浏览器里打开的后台 |
| `cdz_uniapp` | **用户端**。车主用的手机应用，也能在电脑浏览器里看；含商家端分包 |
| `docs` | 三端的功能说明和运行教程 |
| `LICENSE` | Apache License 2.0 全文 |

### **6.2 服务端 `cdz_server`**

| 路径 | 是什么 |
|---|---|
| `src/main/java/com/sqx/SqxApplication.java` | 启动入口。日志里会打印「充电桩项目启动成功」 |
| `src/main/resources` | `application.yml` / `application-prod.yml`：端口、库连接、Redis、JWT |
| `db/chongdianzhuang.sql` | 演示库（全表结构 + 菜单、配置、门店样例；大体量流水表仅结构） |
| `modules/tearoom` | 充电站 / 门店、充电位（房间）、计费与时段相关业务 |
| `modules/orders` | 充电订单 |
| `modules/car` | 用户车辆 |
| `modules/app` | 用户端登录、资料等 App 接口 |
| `modules/pay` / `modules/integral` | 支付、钱包充值档位 |
| `modules/coupon` / `modules/member` / `modules/invite` | 优惠券、会员、邀请分销 |
| `modules/sys` | 管理员、角色、菜单、系统配置 |
| `modules/banner` / `modules/helpCenter` / `modules/message` | 首页装修、帮助、消息与注销 |
| `modules/city` / `modules/apply` / `modules/chats` | 城市、加盟申请、会话相关 |
| `common` / `config` / `datasource` | 公共工具、配置与数据源 |

### **6.3 管理端 `cdz_admin`**

| 路径 | 是什么 |
|---|---|
| `src/views` | 业务与系统页面。用户、订单、门店、优惠券、财务、充值、城市、帮助、系统配置等都在这里 |
| `src/views/modules/sys` | 管理员、角色、菜单 |
| `src/router` | 页面地址和动态菜单对应关系 |
| `src/store` | 登录状态等临时数据 |
| `src/utils` | Axios 请求封装等 |
| `config/index.js` | 开发端口（默认 `8001`）、代理到服务端 |
| `static/config` | 打包后的接口地址、CDN 版本切换 |
| `src/mock` | mock 数据，可切换真实接口 / 模拟数据 |

前后端分离，通过 token 进行数据交互，可独立部署；主题可通过 scss 变量统一定制；菜单由后台「菜单管理」动态下发。

### **6.4 用户端 `cdz_uniapp`**

| 路径 | 是什么 |
|---|---|
| `pages` | 底部三个主入口：首页、订单、我的，以及登录注册、扫码开充、站点详情等 |
| `my` | 分包页面。钱包、车辆、会员、积分、常用充电、订单详情等 |
| `shop` | 商家中心分包。充电位管理、商家订单、时长统计、提现等 |
| `common` | 接口地址（`config.js`）、公共逻辑 |
| `components` | 页面里重复使用的组件 |
| `static` | 图标、默认图片 |
| `uni_modules` / `uview-ui` | 界面组件库（uView） |
| `pages.json` | 有哪些页面、底部导航怎么排 |
| `manifest.json` | 应用名称「充电桩」、微信小程序 AppId 等 |

## **7. 核心特性与技术栈**

### **7.1 核心特性**

· **附近找站、站点详情与收费标准**  
· **扫码开充、确认支付与订单全流程**  
· **我的车辆、钱包充值、优惠券与会员**  
· **邀请分销、积分与帮助中心**  
· **门店加盟、商家充电位与订单运营**  
· **后台数据中心、财务、充值与权限配置**  
· **服务端统一处理登录、订单和支付结果**

每项怎么用，见 [用户端功能说明](docs/用户端-功能说明.md)、[管理端功能说明](docs/管理端-功能说明.md)、[服务端功能说明](docs/服务端-功能说明.md)。

版本以仓库里的配置文件为准。没写死版本号的，按运行教程安装对应大版本即可。

### **7.2 服务端**

| 类别 | 技术 | 版本 | 用来做什么 |
|---|---|---|---|
| 语言 | **Java** | **1.8** | 整个服务端。不能换成 11 或 17 |
| 框架 | **Spring Boot** | **2.1.8.RELEASE** | 提供网页接口，端口 `8174`，路径 `/sqx_fast` |
| 构建 | **Maven** | 3.6 及以上 | 下载依赖、编译、启动 |
| 数据库 | **MySQL** | 5.7 / 8.0 及以上 | 业务数据。配置里库名示例为 `chongdianzhuang` |
| 数据库访问 | **MyBatis-Plus** | 3.2.0 | 读写表 |
| 连接池 | **Druid** | 1.1.x | 管理数据库连接 |
| 缓存 | **Redis** | 可选 | 当前默认关闭；打开后用于缓存等 |
| 管理端登录 | **Apache Shiro** | 1.4.0 | 后台账号、角色、菜单权限 |
| 用户端登录 | **JWT**（jjwt） | 0.7.0 | 用户登录后的凭证 |
| 接口文档 | **Swagger** | 2.7.0 | 浏览器打开 `swagger-ui.html` 查看接口 |
| JSON | **Fastjson** | 1.2.60 | 接口数据格式 |
| 工具库 | **Hutool** | 4.6.10 | 日期、字符串等常用处理 |
| 对象存储 | 阿里云 OSS、七牛、腾讯云 COS | 随模块 | 可选。密钥要自己申请 |
| 支付 | 微信支付 SDK、支付宝 SDK | 随支付模块 | 下单、退款。密钥要自己申请 |
| 实时能力 | **Spring WebSocket** | 随 Spring | 会话 / 通知相关 |

### **7.3 管理端**

| 类别 | 技术 | 版本 | 用来做什么 |
|---|---|---|---|
| 运行环境 | **Node.js** | 18（见 `package.json` engines） | 安装依赖、启动开发网站 |
| 页面框架 | **Vue** | 2.5.16 | 后台每一个页面 |
| 构建 | **Webpack** | 3.x | 开发时热更新，默认端口 `8001` |
| 界面组件 | **Element UI** | 2.8.2 | 表格、表单、弹窗、菜单 |
| 页面跳转 | **Vue Router** | 3.0 | 登录页、统计页和各业务页的地址 |
| 状态 | **Vuex** | 3.0 | 记住是否已登录 |
| 请求 | **Axios** | 0.17 | 把操作发给服务端 |
| 图表 | **ECharts** | 5.x | 数据统计页上的图 |
| 地图 | **vue-baidu-map** | 0.21 | 选点、门店位置 |
| 样式 | **Sass** | 1.32 | 后台样式与主题变量 |

### **7.4 用户端**

| 类别 | 技术 | 版本 | 用来做什么 |
|---|---|---|---|
| 框架 | **uni-app** | 用 HBuilderX 打开 | 同一套页面可运行到浏览器、微信小程序 |
| 开发工具 | **HBuilderX** | 正式版 | 编译并运行到 Chrome 或微信开发者工具 |
| 界面组件 | **uView UI** | 2.0.x | 按钮、输入框、轮播、弹窗 |
| 微信能力 | **weixin-js-sdk** | 1.6 | H5 / 公众号相关能力 |
| 微信小程序 | 微信开发者工具 | 可选 | 要自己的小程序 AppId 才能真机预览 |

## **8. 开源协议**

<div align="center">

本仓库使用 **Apache License 2.0**

全文见 [LICENSE](LICENSE)

</div>

## **9. 社区与支持**

运行、二次开发或商务合作，可以扫码联系 **省钱兄科技**。

使用过程中如果接口起不来、页面打不开，先按本文 **4. 快速开始** 和 `docs` 里的运行教程排查。地图、短信、微信、支付宝等密钥需要自行申请，申请入口在 [用户端功能说明](docs/用户端-功能说明.md) 末尾。仍解决不了时，再扫下面的码说明你的环境和报错。

当前项目正在测试中，如遇到 BUG，可自行修复或者留言反馈，我们将第一时间进行版本迭代。  
欢迎一起交流学习。开源是把源码开放出来，具体落地、定制和长期维护仍由服务来承接。

<div align="center">

<img src="docs/images/community-qr.jpg" alt="省钱兄科技二维码" width="220" />

**省钱兄科技**

</div>
