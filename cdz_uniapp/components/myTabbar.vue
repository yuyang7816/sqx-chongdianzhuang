<template>
	<view>
		<!-- 自定义导航栏 -->
		<u-tabbar activeColor="#5CC477" inactiveColor="#999999" :value="value" :fixed="true" :placeholder="true"
			:safeAreaInsetBottom="false">
			<u-tabbar-item text="首页" iconS="/static/tabber/home-act.png" icon="/static/tabber/home.png"
				@click="click"></u-tabbar-item>
			<u-tabbar-item text="扫码" icon="/static/tabber/fabu.png" iconS="/static/tabber/fabu.png" @click="click"
				:isTq="true"></u-tabbar-item>
			<u-tabbar-item text="我的" iconS="/static/tabber/my-act.png" icon="/static/tabber/my.png"
				@click="click"></u-tabbar-item>
		</u-tabbar>


		<!-- <u-tabbar activeColor="#1EB05F" inactiveColor="#212121" :value="value" :fixed="true" :placeholder="true"
			:safeAreaInsetBottom="true" :zIndex="999999" :placeholderHeight="placeholderHeight">
			<u-tabbar-item text="动态" iconS="/static/tabbar/icon/haodian_s.png" icon="/static/tabbar/icon/haodian.png"
				@click="goSwitch"></u-tabbar-item>
			<u-tabbar-item text="活动" iconS="/static/tabbar/icon/index_s.png" icon="/static/tabbar/icon/index.png"
				@click="goSwitch"></u-tabbar-item>
			<u-tabbar-item :isTq="true" :text="text" iconS="/static/tabbar/icon/addFu.png"
				icon="/static/tabbar/icon/addFu.png" @click="goSwitch"></u-tabbar-item>
			<u-tabbar-item :badge="tagNum" text="消息" iconS="/static/tabbar/icon/message_s.png"
				icon="/static/tabbar/icon/message.png" @click="goSwitch"></u-tabbar-item>
			<u-tabbar-item text="我的" iconS="/static/tabbar/icon/my_s.png" icon="/static/tabbar/icon/my.png"
				@click="goSwitch"></u-tabbar-item>
		</u-tabbar> -->


	</view>
</template>

<script>
	// #ifdef H5
	import wx from 'weixin-js-sdk'
	// #endif
	export default {
		name: "myTabbar",
		props: {
			text: {
				type: String,
				default: ''
			},
			value: {
				type: Number,
				default: 0
			},
			placeholderHeight: {
				type: Number,
				default: 0
			},
		},
		data() {
			return {
				tsShow: false,
				releaseShow: false,


				XCXIsSelect: '是'
			};
		},
		mounted() {


		},
		created() {
			this.$Request.getT('/app/common/type/302').then(res => {
				if (res.code == 0) {
					if (res.data && res.data.value) {
						// #ifdef MP-WEIXIN
						this.$queue.setData('XCXIsSelect', res.data.value)
						this.$queue.setData('shangxianSelect', res.data.value)
						// #endif
						// #ifndef MP-WEIXIN
						this.$queue.setData('XCXIsSelect', '是')
						this.$queue.setData('shangxianSelect', '是')
						// #endif
						this.XCXIsSelect = this.$queue.getData("XCXIsSelect")
					}
				}
			});



		},
		methods: {
			click(e) {
				console.log(e)
				if (e == 0) { //首页
					uni.switchTab({
						url: '/pages/index/index'
					})
				} else if (e == 2) { //我的
					uni.switchTab({
						url: '/pages/my/index'
					})
				} else if (e == 1) { //扫一扫
					let that = this
					if (!uni.getStorageSync('token')) {
						uni.navigateTo({
							url: '/pages/public/login'
						})
						return
					}
					uni.hideLoading({
						title: '加载中...',
						icon: 'none'
					})
					// #ifdef H5
					let url = window.location.href.split('#')[0]
					// alert(url)
					that.$Request.postJson("/app/Login/wxJsApi", {
						url: url
					}).then(red => {
						if (red.code == 0) {
							wx.config({
								debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
								appId: red.data.appId, // 必填，公众号的唯一标识
								timestamp: red.data.timestamp, // 必填，生成签名的时间戳
								nonceStr: red.data.nonceStr, // 必填，生成签名的随机串
								signature: red.data.signature, // 必填，签名
								jsApiList: ['scanQRCode'] // 必填，需要使用的JS接口列表
							});
							// config信息验证后会执行ready方法，所有接口调用都必须在config接口获得结果之后，config是一个客户端的异步操作，所以如果需要在页面加载时就调用相关接口，则须把相关接口放在ready函数中调用来确保正确执行。对于用户触发时才调用的接口，则可以直接调用，不需要放在ready函数中。
							wx.ready(function() {
								wx.scanQRCode({
									needResult: 1, // 默认为0，扫描结果由微信处理，1则直接返回扫描结果，
									scanType: ["qrCode", "barCode"], // 可以指定扫二维码还是一维码，默认二者都有
									success: function(res) {
										var result = res.resultStr; // 当needResult 为 1 时，扫码返回的结果
										// alert(result)
									},
									fail: function(res) {
										console.log('扫描失败!' + res.errMsg);
									},
									complete: (ret) => {
										uni.navigateTo({
											url: '/pages/index/orderDet?scene=20,40,1'
										})
									}
								});
							});
							wx.error(function(res) {

							});
						}
					});

					// #endif
					// #ifndef H5
					// 允许从相机和相册扫码
					uni.scanCode({
						onlyFromCamera: true,
						success: function(res) {
							console.log(res, '扫码')
							console.log('条码类型：' + res.scanType);
							console.log('条码内容：' + res.result);
							console.log(res.path, decodeURIComponent(res.path))
							// #ifdef MP-WEIXIN
							if (res.path) {
								uni.navigateTo({
									url: '/pages/index/orderDet?scene=20,40,1'
								})
							}
							// #endif

						}
					});
					// #endif
				}
			}
		}
	}
</script>

<style lang="scss" scoped>

</style>