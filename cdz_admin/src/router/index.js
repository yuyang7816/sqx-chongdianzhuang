/**
 * 全站路由配置
 *
 * 建议:
 * 1. 代码中路由统一使用name属性跳转(不使用path属性)
 */
import Vue from 'vue'
import Router from 'vue-router'
import http from '@/utils/httpRequest'
import {isURL} from '@/utils/validate'
import {clearLoginInfo} from '@/utils'

Vue.use(Router)

// 开发环境不使用懒加载, 因为懒加载页面太多的话会造成webpack热更新太慢, 所以只有生产环境使用懒加载
const _import = require('./import-' + process.env.NODE_ENV)

// 全局路由(无需嵌套上左右整体布局)
const globalRoutes = [
	{path: '/404', component: _import('common/404'), name: '404', meta: {title: '404未找到'}},
	{path: '/login', component: _import('common/login'), name: 'login', meta: {title: '登录'}}
]

// 主入口路由(需嵌套上左右整体布局)
const mainRoutes = {
	path: '/',
	component: _import('main'),
	name: 'main',
	redirect: {name: 'home'},
	meta: {title: '主入口整体布局'},
	children: [
		// 通过meta对象设置路由展示方式
		// 1. isTab: 是否通过tab展示内容, true: 是, false: 否
		// 2. iframeUrl: 是否通过iframe嵌套展示内容, '以http[s]://开头': 是, '': 否
		// 提示: 如需要通过iframe嵌套展示内容, 但不通过tab打开, 请自行创建组件使用iframe处理!
		{path: '/home', component: _import('common/home'), name: 'home', meta: {title: '首页'}},
		{path: '/userList', component: _import('user/userList'), name: 'userList', meta: {title: '用户列表', isTab: true}},
		{path: '/allocationList',component: _import('allocation/allocationList'),name: 'allocationList',meta: {title: '配置列表', isTab: true}},
		{path: '/orderCenter',component: _import('orderCenter/orderCenter'),name: 'orderCenter',meta: {title: '订单中心',isTab: true}},
		{path: '/financeList', component: _import('finance/financeList'), name: 'financeList', meta: {title: '财务中心', isTab: true}},
		{path: '/message', component: _import('message/message'), name: 'message', meta: {title: '消息中心', isTab: true}},
		{path: '/userDetail', component: _import('user/userDetail'), name: 'userDetail', meta: {title: '用户详情', isTab: false}},
		{path: '/userDetail1', component: _import('user/userDetail1'), name: 'userDetail1', meta: {title: '用户详情', isTab: false}},
		{path: '/bannerList', component: _import('banner/bannerList'), name: 'bannerList', meta: {title: '商城配置', isTab: true}},
		{path: '/materialsList', component: _import('materials/materialsList'), name: 'materialsList', meta: {title: '帮助中心', isTab: true}},
		{ path: '/comment', component: _import('comment/comment'), name: 'comment', meta: { title: '评价中心',isTab: true} },
		//  以下为新增的
		{ path: '/wechatList', component: _import('wechat/wechatList'), name: 'wechatList', meta: { title: '微信配置',isTab: true} },
		{ path: '/classifyAdmin', component: _import('selfShop/classifyAdmin'), name: 'classifyAdmin', meta: { title: '商品分类',isTab: true} },
		{ path: '/brandShop', component: _import('selfShop/brandShop'), name: 'brandShop', meta: { title: '商品品牌',isTab: true} },
		{ path: '/shopAdmin', component: _import('selfShop/shopAdmin'), name: 'shopAdmin', meta: { title: '商品管理',isTab: true} },
		{ path: '/shopAdmin1', component: _import('selfShop/shopAdmin1'), name: 'shopAdmin1', meta: { title: '商品管理',isTab: false} },
		{ path: '/specification', component: _import('selfShop/specification'), name: 'specification', meta: { title: '商品规格',isTab: true} },
		{ path: '/shopPublish', component: _import('selfShop/shopPublish'), name: 'shopPublish', meta: { title: '发布商品',isTab: false} },
		{ path: '/shopAmend', component: _import('selfShop/shopAmend'), name: 'shopAmend', meta: { title: '修改商品',isTab: false} },
		{ path: '/orderDetails', component: _import('selfShop/orderDetails'), name: 'orderDetails', meta: { title: '订单详情',isTab: true} },
		{ path: '/discuss', component: _import('selfShop/discuss'), name: 'discuss', meta: { title: '商品评论',isTab: true} },
		{ path: '/virtual', component: _import('virtual/virtual'), name: 'virtual', meta: { title: '虚拟商品',isTab: true} },
		{ path: '/virtualAmend', component: _import('virtual/virtualAmend'), name: 'virtualAmend', meta: { title: '兑换码列表',isTab: false} },
		// { path: '/virtualAmend', component: _import('virtual/virtualAmend'), name: 'virtualAmend', meta: { title: '兑换码列表',isTab: true} },
		{ path: '/coupon', component: _import('coupon/coupon'), name: 'coupon', meta: { title: '优惠券制作',isTab: true} },
		{ path: '/couponissue', component: _import('coupon/couponissue'), name: 'couponissue', meta: { title: '已发布优惠券',isTab: true} },
		{ path: '/couponuser', component: _import('coupon/couponuser'), name: 'couponuser', meta: { title: '优惠券领取记录',isTab: true} },
		{ path: '/couponList', component: _import('coupon/couponList'), name: 'couponList', meta: { title: '卡密管理',isTab: true} },
		{ path: '/couponTg', component: _import('coupon/couponTg'), name: 'couponTg', meta: { title: '兑换列表',isTab: true} },
		// 商家模块
		{ path: '/merchantList', component: _import('merchant/merchantList'), name: 'merchantList', meta: { title: '店铺列表',isTab: true} },
		{ path: '/merchantEnter', component: _import('merchant/merchantEnter'), name: 'merchantEnter', meta: { title: '入驻申请',isTab: true} },
		{ path: '/merchDetails', component: _import('merchant/merchDetails'), name: 'merchDetails', meta: { title: '审核详情',isTab: true} },
		{ path: '/merchIncome', component: _import('merchant/merchIncome'), name: 'merchIncome', meta: { title: '台球馆详情',isTab: false} },
		{ path: '/complaint', component: _import('merchant/complaint'), name: 'complaint', meta: { title: '投诉管理',isTab: true} },
		{ path: '/merchantDeposit', component: _import('merchant/merchantDeposit'), name: 'merchantDeposit', meta: { title: '保证金',isTab: true} },
		{ path: '/shopPublish2', component: _import('merchant/shopPublish2'), name: 'shopPublish2', meta: { title: '发布商品',isTab: false} },
		{ path: '/shopAmend2', component: _import('merchant/shopAmend2'), name: 'shopAmend2', meta: { title: '修改商品',isTab: false} },
		//8.15 新增茶 室设备列表
		{ path: '/devices', component: _import('merchant/devices'), name: 'devices', meta: { title: '设备列表',isTab: true} },
		{ path: '/devicedetails', component: _import('merchant/devicedetails'), name: 'devicedetails', meta: { title: '设备详情',isTab: false} },
		{path: '/messageZx', component: _import('message/messageZx'), name: 'messageZx', meta: {title: '注销信息', isTab: true}},
		{path: '/app', component: _import('app/app'), name: 'app', meta: { title: '升级配置', isTab: true } },
		 {path: '/citys', component: _import('vipPrivilege/citys'), name: 'citys', meta: { title: '城市管理',isTab: true} },
		// 2024.03.08 新增
		{path: '/IntegralGoods', component: _import('integral/IntegralGoods'), name: 'IntegralGoods', meta: {title: '充值配置', isTab: true}},
		{path: '/exchangeList', component: _import('integral/exchangeList'), name: 'exchangeList', meta: {title: '充值记录', isTab: true}},
		{ path: '/couponFk', component: _import('integral/coupon'), name: 'couponFk', meta: { title: '发卡管理',isTab: true} },
		// 2024.03.07 无人柜子新增
		{ path: '/commodity', component: _import('commodity/commodity'), name: 'commodity', meta: { title: '展示柜商品管理',isTab: false} },
		{ path: '/displayCabinet', component: _import('commodity/displayCabinet'), name: 'displayCabinet', meta: { title: '展示柜管理',isTab: false} },
		{ path: '/displayCabinetTwo', component: _import('commodity/displayCabinetTwo'), name: 'displayCabinetTwo', meta: { title: '展示柜设备',isTab: true} },
		// 2025.02.10 星际球 新增
		{ path: '/monthlyCard', component: _import('monthlyCard/monthlyCard'), name: 'monthlyCard', meta: { title: '卡券管理',isTab: true} },
		{ path: '/monthlyCardList', component: _import('monthlyCard/monthlyCardList'), name: 'monthlyCardList', meta: { title: '卡券赠送列表',isTab: true} },
		{ path: '/couponKm', component: _import('monthlyCard/coupon'), name: 'couponKm', meta: { title: '卡密管理',isTab: true} },
		{ path: '/paihang', component: _import('monthlyCard/paihang'), name: 'paihang', meta: { title: '开台排行',isTab: true} },
		{path: '/points', component: _import('integral/points'), name: 'points', meta: {title: '积分明细', isTab: true}},
		{path: '/memberDetails',component: _import('members/memberDetails'),name: 'memberDetails',meta: {title: '会员配置',isTab: true}},
		{path: '/vipPrivilege',component: _import('members/vipPrivilege'),name: 'vipPrivilege',meta: {title: '会员特权',isTab: true}},
		{path: '/locality', component: _import('eventManagement/locality'), name: 'locality', meta: {title: '活动管理', isTab: true}},
		{path: '/ongoingActivities', component: _import('eventManagement/ongoingActivities'), name: 'ongoingActivities', meta: {title: '进行中活动', isTab: true}},
		{path: '/signIn', component: _import('eventManagement/signIn'), name: 'signIn', meta: {title: '签到列表', isTab: true}},
		{path: '/VideoList', component: _import('orderCenter/VideoList'), name: 'VideoList', meta: {title: '视频列表', isTab: true}},
		{path: '/autonym', component: _import('autonym/autonym'), name: 'autonym', meta: {title: '实名认证', isTab: true}},
		{ path: '/timedGiftCoupon', component: _import('coupon/timedGiftCoupon'), name: 'timedGiftCoupon', meta: { title: '优惠券活动',isTab: true} },
		{path: '/activityType', component: _import('eventManagement/activityType'), name: 'activityType', meta: {title: '活动类型', isTab: true}},
		{ path: '/xiaofei', component: _import('monthlyCard/xiaofei'), name: 'xiaofei', meta: { title: '消费排行',isTab: true} },
		// 2025.03.24 台球3.0 新增
		{path: '/shopAdminZ', component: _import('commodityManagement/shopAdminZ'), name: 'shopAdminZ', meta: {title: '商品管理', isTab: true}},
		{path: '/specificationZ', component: _import('commodityManagement/specificationZ'), name: 'specificationZ', meta: {title: '商品规格', isTab: true}},
		{path: '/classifyAdminZ', component: _import('commodityManagement/classifyAdminZ'), name: 'classifyAdminZ', meta: {title: '商品分类', isTab: true}},
		{path: '/shopPublishS', component: _import('commodityManagement/shopPublish'), name: 'shopPublishS', meta: {title: '发布商品', isTab: false}},
		{path: '/shopAmendS', component: _import('commodityManagement/shopAmend'), name: 'shopAmendS', meta: {title: '修改商品', isTab: false}},
		{path: '/specificationS', component: _import('commodityManagement/specification'), name: 'specificationS', meta: {title: '商品规格', isTab: false}},
		{path: '/classifyAdminS', component: _import('commodityManagement/classifyAdmin'), name: 'classifyAdminS', meta: {title: '商品分类', isTab: false}},
		{path: '/shopsListSm', component: _import('commodityManagement/shopsList'), name: 'shopsListSm', meta: {title: '店铺中心', isTab: true}},
		{path: '/tableYoncanSm', component: _import('commodityManagement/tableYoncan'), name: 'tableYoncanSm', meta: {title: '用餐管理', isTab: true}},
		{path: '/shopMoneySm', component: _import('commodityManagement/shopMoney'), name: 'shopMoneySm', meta: {title: '收银台数据', isTab: true}},
		{path: '/shopsListAdminSm', component: _import('commodityManagement/shopAdmin'), name: 'shopsListAdminSm', meta: {title: '门店商品管理', isTab: false}},
		{path: '/missionSm', component: _import('commodityManagement/mission'), name: 'missionSm', meta: { title: '商铺订单',isTab: true} },
		{path: '/shopsOrderSm', component: _import('commodityManagement/shopsOrder'), name: 'shopsOrderSm', meta: { title: '门店订单',isTab: false} },
		{path: '/tableCode', component: _import('commodityManagement/tableCode'), name: 'tableCode', meta: {title: '桌码管理', isTab: true}},
		// 2025.04.03 新增
		{ path: '/chatRecord',component: _import('chatRecord/chatRecord'),name: 'chatRecord',meta: {title: '聊天记录',isTab: true}},
		// 2025.04.03 台球4.0 新增
		{ path: '/classifyAdminZy', component: _import('selfShopZy/classifyAdmin'), name: 'classifyAdminZy', meta: { title: '商品分类',isTab: true} },
		{ path: '/orderAdminZy', component: _import('selfShopZy/orderAdmin'), name: 'orderAdminZy', meta: { title: '自营订单',isTab: true} },
		{ path: '/addressAaminZy', component: _import('selfShopZy/addressAamin'), name: 'addressAaminZy', meta: { title: '地址管理',isTab: true} },
		{ path: '/shopAdminZy', component: _import('selfShopZy/shopAdmin'), name: 'shopAdminZy', meta: { title: '商品管理',isTab: true} },
		{ path: '/fenxiaoOrderZy', component: _import('selfShopZy/fenxiaoOrder'), name: 'fenxiaoOrderZy', meta: { title: '分销订单',isTab: true}, },
		{ path: '/disposeOrderZy', component: _import('selfShopZy/disposeOrder'), name: 'disposeOrderZy', meta: { title: '待处理订单',isTab: true}, },
		{ path: '/shopConfigZy', component: _import('selfShopZy/shopConfig'), name: 'shopConfigZy', meta: { title: '自营商城配置',isTab: true} },
		{ path: '/shopPublishZy', component: _import('selfShopZy/shopPublish'), name: 'shopPublishZy', meta: { title: '发布商品',isTab: false} },
		{ path: '/shopAmendZy', component: _import('selfShopZy/shopAmend'), name: 'shopAmendZy', meta: { title: '修改商品',isTab: false} },
		{ path: '/specificationZy', component: _import('selfShopZy/specification'), name: 'specificationZy', meta: { title: '商品规格',isTab: true} },
		{ path: '/orderDetailsZy', component: _import('selfShopZy/orderDetails'), name: 'orderDetailsZy', meta: { title: '订单详情',isTab: false} },
		{ path: '/discussZy', component: _import('selfShopZy/discuss'), name: 'discussZy', meta: { title: '评价列表',isTab: true} },
		{ path: '/taskConfig1Zy', component: _import('selfShopZy/taskConfig'), name: 'taskConfig1Zy', meta: { title: '商城配置',isTab: true} },
		{ path: '/dynamic', component: _import('topicForum/dynamic'), name: 'dynamic', meta: {title: '动态管理', isTab: true}},
		{path: '/comments', component: _import('materials/comments'), name: 'comments', meta: {title: '动态评论库', isTab: true}},
		{path: '/messageBoard', component: _import('topicForum/messageBoard'), name: 'messageBoard', meta: {title: '留言板管理', isTab: true}},
		{path: '/aboutTheBall', component: _import('topicForum/aboutTheBall'), name: 'aboutTheBall', meta: {title: '约球记录', isTab: true}},
		// 2025.05.07  台球5.0 新增 教练模块
		{path: '/divideIntoJl', component: _import('coach/divideInto'), name: 'divideIntoJl', meta: {title: '教练分成规则', isTab: true}},
		{path: '/applyListJl', component: _import('coach/applyList'), name: 'applyListJl', meta: { title: '教练申请列表',isTab: true} },
		{path: '/technicianJl', component: _import('coach/technician'), name: 'technicianJl', meta: { title: '教练列表',isTab: true} },
		{path: '/cityVotingJl',component: _import('coach/cityVoting'),name: 'cityVotingJl',meta: {title: '城市投票',isTab: true}},
		{path: '/signInJl', component: _import('coach/signIn'), name: 'signInJl', meta: { title: '教练签到记录',isTab: true} },
		{path: '/riderTopJl', component: _import('coach/riderTop'), name: 'riderTopJl', meta: {title: '教练收益排行榜', isTab: true}},
		{path: '/topJl', component: _import('coach/top'), name: 'topJl', meta: {title: '教练信用分排行榜', isTab: true}},
		{path: '/creditScoreJl', component: _import('coach/creditScore'), name: 'creditScoreJl', meta: {title: '教练信用分明细', isTab: true}},
		{path: '/autonymJl', component: _import('coach/autonym'), name: 'autonymJl', meta: {title: '教练认证', isTab: true}},
		{path: '/shifuSchedulingJl',component: _import('coach/shifuScheduling'),name: 'shifuSchedulingJl',meta: {title: '教练地图', isTab: true}},
		{path: '/masterPositionJl', component: _import('coach/masterPosition'), name: 'masterPositionJl', meta: {title: '教练位置记录', isTab: true}},
		{path: '/orderInServiceJl', component: _import('coach/orderInService'), name: 'orderInServiceJl', meta: {title: '教练服务中订单', isTab: true}},
		{path: '/localityJl', component: _import('coach/locality'), name: 'localityJl', meta: {title: '教练项目中心', isTab: true}},
		{path: '/orderCenterJl',component: _import('coach/orderCenter'),name: 'orderCenterJl',meta: {title: '教练订单列表',isTab: true}},
		{path: '/business', component: _import('coach/business'), name: 'business', meta: {title: '投诉中心', isTab: true}},
		{path: '/travel', component: _import('coach/travel'), name: 'travel', meta: {title: '出行配置', isTab: true}},
		// 柜子模块
		{path: '/cabinetList',component: _import('cabinet/cabinetList'),name: 'cabinetList',meta: {title: '租柜列表',isTab: true}},
		{path: '/cabinetOrder',component: _import('cabinet/cabinetOrder'),name: 'cabinetOrder',meta: {title: '租柜订单',isTab: true}},
		// 2025.05.14 新增 爱心模块
		{path: '/loveRecord', component: _import('loveModule/loveRecord'), name: 'loveRecord', meta: {title: '爱心钱包排行榜', isTab: true}},
		{path: '/lovePublicity', component: _import('loveModule/lovePublicity'), name: 'lovePublicity', meta: {title: '爱心公示', isTab: true}},
		// 2025.06.16 新增
		{path: '/faceRecognition', component: _import('message/faceRecognition'), name: 'faceRecognition', meta: {title: '人脸识别记录', isTab: true}},
		// 2025.07.03 新增
		{ path: '/AIkeyword',component: _import('chatRecord/AIkeyword'),name: 'AIkeyword',meta: {title: 'AI关键词配置',isTab: true}},
		// 2025.07.15 新增
		{path: '/musicActivity', component: _import('music/musicActivity'), name: 'musicActivity', meta: {title: '音乐分类', isTab: true}},
		{path: '/musicLibrary', component: _import('music/musicLibrary'), name: 'musicLibrary', meta: {title: '音乐列表', isTab: true}},
		{path: '/musicPlay', component: _import('music/musicPlay'), name: 'musicPlay', meta: {title: '点歌列表', isTab: true}},
		// 2025.07.17 新增
		{path: '/localLife', component: _import('topicForum/localLife'), name: 'localLife', meta: {title: '本地生活', isTab: true}},
		// 2025.10.15 新增
		{path: '/transferGift', component: _import('integral/transferGift'), name: 'transferGift', meta: {title: '转赠记录', isTab: true}},
		

	],
	beforeEnter (to, from, next) {
		let token = Vue.cookie.get('token')
		if (!token || !/\S/.test(token)) {
			clearLoginInfo()
			next({name: 'login'})
		}
		next()
	}
}

const router = new Router({
	mode: 'history', //  hash
	scrollBehavior: () => ({y: 0}),
	isAddDynamicMenuRoutes: false, // 是否已经添加动态(菜单)路由
	routes: globalRoutes.concat(mainRoutes)
})

router.beforeEach((to, from, next) => {
	let userId = Vue.cookie.get('userId')
	// 添加动态(菜单)路由
	// 1. 已经添加 or 全局路由, 直接访问
	// 2. 获取菜单列表, 添加并保存本地存储
	if (userId==''||router.options.isAddDynamicMenuRoutes || fnCurrentRouteType(to, globalRoutes) === 'global') {
		next()
	} else {
		http({
			url: http.adornUrl('/sys/menu/nav'),
			method: 'get',
			params: http.adornParams()
		}).then(({data}) => {
			if (data && data.code === 0) {
				fnAddDynamicMenuRoutes(data.menuList)
				router.options.isAddDynamicMenuRoutes = true
				sessionStorage.setItem('menuList', JSON.stringify(data.menuList || '[]'))
				sessionStorage.setItem('permissions', JSON.stringify(data.permissions || '[]'))
				next({...to, replace: true})
			} else {
				sessionStorage.setItem('menuList', '[]')
				sessionStorage.setItem('permissions', '[]')
				next()
			}
		}).catch((e) => {
			console.log(`%c${e} 请求菜单列表和权限失败，跳转至登录页！！`, 'color:blue')
			router.push({name: 'login'})
		})
	}
})

/**
 * 判断当前路由类型, global: 全局路由, main: 主入口路由
 * @param {*} route 当前路由
 */
function fnCurrentRouteType (route, globalRoutes = []) {
	var temp = []
	for (var i = 0; i < globalRoutes.length; i++) {
		if (route.path === globalRoutes[i].path) {
			return 'global'
		} else if (globalRoutes[i].children && globalRoutes[i].children.length >= 1) {
			temp = temp.concat(globalRoutes[i].children)
		}
	}
	return temp.length >= 1 ? fnCurrentRouteType(route, temp) : 'main'
}

/**
 * 添加动态(菜单)路由
 * @param {*} menuList 菜单列表
 * @param {*} routes 递归创建的动态(菜单)路由
 */
function fnAddDynamicMenuRoutes (menuList = [], routes = []) {
	var temp = []
	for (var i = 0; i < menuList.length; i++) {
		if (menuList[i].list && menuList[i].list.length >= 1) {
			temp = temp.concat(menuList[i].list)
		} else if (menuList[i].url && /\S/.test(menuList[i].url)) {
			menuList[i].url = menuList[i].url.replace(/^\//, '')
			var route = {
				path: menuList[i].url.replace('/', '-'),
				component: null,
				name: menuList[i].url.replace('/', '-'),
				meta: {
					menuId: menuList[i].menuId,
					title: menuList[i].name,
					isDynamic: true,
					isTab: true,
					iframeUrl: ''
				}
			}
			// url以http[s]://开头, 通过iframe展示
			if (isURL(menuList[i].url)) {
				route['path'] = `i-${menuList[i].menuId}`
				route['name'] = `i-${menuList[i].menuId}`
				route['meta']['iframeUrl'] = menuList[i].url
			} else {
				try {
					route['component'] = _import(`modules/${menuList[i].url}`) || null
				} catch (e) {
				}
			}
			routes.push(route)
		}
	}
	if (temp.length >= 1) {
		fnAddDynamicMenuRoutes(temp, routes)
	} else {
		mainRoutes.name = 'main-dynamic'
		mainRoutes.children = routes
		router.addRoutes([
			mainRoutes,
			{path: '*', redirect: {name: '404'}}
		])
		sessionStorage.setItem('dynamicMenuRoutes', JSON.stringify(mainRoutes.children || '[]'))
		console.log('\n')
		console.log('%c!<-------------------- 动态(菜单)路由 s -------------------->', 'color:blue')
		console.log(mainRoutes.children)
		console.log('%c!<-------------------- 动态(菜单)路由 e -------------------->', 'color:blue')
	}
}

export default router
