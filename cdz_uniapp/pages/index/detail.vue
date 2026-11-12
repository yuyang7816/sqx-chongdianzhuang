<template>
	<view class="">
		<!-- 2.0.19支持autoBack，默认为false -->
		<u-navbar :title="title" :bgColor="bgColor" placeholder autoBack>
		</u-navbar>

		<view class="bgColorTop">

			<view class="">
				<scroll-view class="scroll-view_H" scroll-x="true" scroll-left="0">
					<view :id="'demo'+index" class="scroll-view-item_H"
						v-for="(item,index) in infoIte.tearoomTitleImage" :key="index"
						@click="previewDetailImage(index)">
						<image :src="item" mode="aspectFill"></image>
					</view>
				</scroll-view>
			</view>

			<view class="addbox">
				<view class="padding">
					<view class="addboxTitle">{{infoIte.tearoomName}}</view>
					<view class="flex align-center" v-if="infoIte.tearoomLabel">
						<view class="addboxTit" v-for="(item,index) in infoIte.tearoomLabel" :key="index">
							{{item}}
							<text v-if="infoIte.tearoomLabel.length-1>index"></text>
						</view>
					</view>
				</view>
				<view class="addboxAdd" @click="openMap()">
					<view class="addboxAdd_info">
						<image src="/static/images/index/dh.png"></image>
						<view>{{infoIte.tearoomAddress}}</view>
					</view>
					<image src="/static/images/index/qdh.png"></image>
				</view>
			</view>
		</view>

		<view class="tabbox" v-if="current==1">
			<view class="tabboxIte" style="background: #32BE62;" @click="bindCar(0)">
				<view class="tabboxIte_size" style="color: #32BE62;">快</view>
				<view class="tabboxIteTit">
					空闲{{(infoIte.kuaiCount||0)-(infoIte.kuaiCount2||0)}}
					<text></text>
					共{{infoIte.kuaiCount||0}}
				</view>
				<view class="triangle-down" style="border-top:16rpx solid #32BE62;" v-if="carIndex==0"></view>
			</view>
			<view class="tabboxIte" style="background: #00A9EE;" @click="bindCar(1)">
				<view class="tabboxIte_size" style="color: #00A9EE;">慢</view>
				<view class="tabboxIteTit">
					空闲{{(infoIte.manCount||0)-(infoIte.manCount2||0)}}
					<text></text>
					共{{infoIte.manCount||0}}
				</view>
				<view class="triangle-down" style="border-top:16rpx solid #00A9EE;" v-if="carIndex==1"></view>
			</view>
		</view>

		<view class="skubox" v-if="current==1">
			<scroll-view class="scroll-view_HSku" scroll-x="true" scroll-left="30">
				<view class="skubox_ite" v-for="(item,index) in list" :key="index"
					:class="item.goodsStatus==2?'skubox_iteAct':''">
					<view class="skubox_iteNum">{{item.goodsName}}</view>
					<view class="skubox_iteTit" v-if="item.status==1">{{item.goodsStatus==2?'充电中':'空闲'}}</view>
					<view class="skubox_iteTit" v-if="item.status==2">故障</view>

				</view>
			</scroll-view>

			<!-- <view class="skubox_detail">详情
				<u-icon name="arrow-right" color="#333333" size="20"></u-icon>
			</view> -->
		</view>

		<view class="Charging" v-if="current==0">
			<view class="Charging_Head">
				<view class="Charging_HeadTitle">充电插口</view>
				<view class="Charging_HeadTit" @click="goSfbz()">收费标准
					<u-icon name="arrow-right" color="#999999" size="16"></u-icon>
				</view>
			</view>
			<view class="flex flex-wrap justify-between margin-top">
				<view class="Charging_ite" v-for="(item,index) in list" :key="index"
					:class="item.goodsStatus==2?'Charging_iteAct':item.status==2?'Charging_iteActs':listIndex==index?'Charging_iteActl':''">
					<view class="Charging_iteNum" :style="item.status==1&&item.goodsStatus==2?'margin-top:10rpx;':''">
						{{item.goodsName}}
					</view>
					<view class="Charging_iteTit" v-if="item.status==1">{{item.goodsStatus==2?'充电中':'空闲'}}</view>
					<view class="Charging_iteTit" v-if="item.status==2">故障</view>
					<!-- <view class="Charging_iteData" v-if="item.status==1&&item.goodsStatus==2">04:13:05结束</view> -->
					<view class="Charging_iteData" v-if="item.status==1&&item.goodsStatus==2">
						<u-count-down :time="item.timeDiff" format="HH:mm:ss"></u-count-down>结束
					</view>

				</view>
				<view class="Charging_ite" style="border: none;height: 0;margin: 0;"></view>
				<view class="Charging_ite" style="border: none;height: 0;margin: 0;"></view>
				<view class="Charging_ite" style="border: none;height: 0;margin: 0;"></view>
			</view>


		</view>

		<view class="pricebox" v-if="current==0">
			<view class="priceTop">
				<view class="priceboxTitle">价格信息</view>
				<view class="priceTop_more" @click="show = true">
					查看更多
					<u-icon name="arrow-right" color="#999999" size="16"></u-icon>
				</view>
			</view>
			<view class="priceboxInfo">
				<view class="priceboxInfo_p">
					<view class="priceboxInfo_pTit">当前价</view>
					<view class="priceboxInfo_pNum"><text>{{priceInfo.hourPrice}}</text>元/时</view>
				</view>
				<view class="priceboxInfo_vip">
					<view class="flex align-center justify-between">
						<view class="priceboxInfo_vip_pTit">会员价</view>
						<view class="priceboxInfo_vip_pbtn" @click="goVip()">开通会员</view>
					</view>
					<view class="priceboxInfo_vip_pNum"><text>{{priceInfo.memberHourPrice}}</text>元/时</view>
				</view>
			</view>
		</view>

		<view class="pricebox" v-if="current==1">
			<view class="priceTop">
				<view class="priceboxTitle">价格信息</view>
				<view class="priceTop_more" @click="show = true">
					查看更多
					<u-icon name="arrow-right" color="#999999" size="16"></u-icon>
				</view>
			</view>
			<view class="priceboxInfo">
				<view class="priceboxInfo_p">
					<view class="priceboxInfo_pTit">当前价</view>
					<view class="priceboxInfo_pNum"><text>{{priceInfo.electricityPrice}}</text>元/度</view>
				</view>
				<view class="priceboxInfo_vip">
					<view class="flex align-center justify-between">
						<view class="priceboxInfo_vip_pTit">会员价</view>
						<view class="priceboxInfo_vip_pbtn" @click="goVip()">开通会员</view>
					</view>
					<view class="priceboxInfo_vip_pNum"><text>{{priceInfo.memberElectricityPrice}}</text>元/度</view>
				</view>
			</view>
		</view>

		<view class="tipbox" v-if="current==1">
			<view class="tipboxTitle">停车费描述</view>
			<view class="tipboxTit" v-html="infoIte.tearoomContent">

			</view>
		</view>

		<view class="btn" @click="goOrderDet()">
			扫码充电
		</view>

		<u-popup :show="show" @close="show = false" :round="18">
			<view class="popuI">
				<view class="popuI_title">价格详情</view>
				<view class="popuITop ">
					<view class="shiduan padding-left">充电时段</view>
					<view class="type">类型</view>
					<view class="jiage">价格 (元/{{current==0?'时':'度'}})</view>
				</view>
				<scroll-view scroll-y="true" class="scroll-Y" v-if="current==0">
					<view class="popuItem" v-for="(item,index) in priceList" :key="index">
						<view class="shiduan">{{item.startTime}}-{{item.endTime}}</view>
						<view class="type">
							<view class="yjtit">原价</view>
							<view class="hyjtit">会员价</view>
						</view>
						<view class="jiage">
							<view class="yj">{{item.hourPrice}}</view>
							<view class="hyj">{{item.memberHourPrice?item.memberHourPrice:0}}</view>
						</view>
					</view>
				</scroll-view>
				<scroll-view scroll-y="true" class="scroll-Y" v-if="current==1">
					<view class="popuItem" v-for="(item,index) in priceList" :key="index">
						<view class="shiduan">{{item.startTime}}-{{item.endTime}}</view>
						<view class="type">
							<view class="yjtit">原价</view>
							<view class="hyjtit">会员价</view>
						</view>
						<view class="jiage">
							<view class="yj">{{item.hourPrice}}</view>
							<view class="hyj">{{item.memberElectricityPrice?item.memberElectricityPrice:0}}</view>
						</view>
					</view>
				</scroll-view>



			</view>
		</u-popup>
	</view>
</template>

<script>
	// #ifdef H5
	import wx from 'weixin-js-sdk'
	// #endif
	export default {
		data() {
			return {
				title: '',
				bgColor: 'linear-gradient(93deg, #E1FFF4 0%, #EBFEF6 37%, #F6FFF7 100%)',
				carIndex: 1,
				skuIndex: 2,
				current: '',
				tearoomId: '',
				infoIte: {},
				list: [],
				listIndex: -1,
				priceInfo: {}, //价格
				priceList: [], //价格
				show: false,
				goodsId: ''


			}
		},
		onLoad(option) {
			uni.showLoading({
				title: '加载中...'
			})
			if (option.current) { // 0 电瓶车 1汽车桩
				this.current = option.current
				if (option.current == 0) {
					this.title = '电瓶车充电桩'
				} else if (option.current == 1) {
					this.title = '汽车充电桩'
				}
			}
			if (option.tearoomId) {
				this.tearoomId = option.tearoomId

			}
		},
		onShow() {
			if (this.tearoomId) {
				this.getDetail()
				this.getlist()
				this.getPrice()
				this.getPricelist()
			}
		},
		methods: {
			goVip() {
				console.log('8888')
				uni.navigateTo({
					url: '/my/vip/index'
				})
			},
			goSfbz() {
				if (this.infoIte.tearoomContent) {
					uni.navigateTo({
						url: '/pages/member/sfbz?content=' + this.infoIte.tearoomContent
					})
				} else {
					uni.showToast({
						title: '暂无收费标准',
						icon: 'none'
					})
				}

			},
			goOrderDet() {
				uni.navigateTo({
					url: '/pages/index/orderDet?tearoomId=' + this.tearoomId + '&goodsId=' + this.goodsId +
						'&current=' + this.current
				})
				return
				// #ifdef H5

				let that = this
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
									// var result = res.resultStr; // 当needResult 为 1 时，扫码返回的结果
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
			},
			bindCar(carIndex) {
				this.carIndex = carIndex
				this.getlist()
			},
			previewDetailImage(index) {
				const urls = Array.isArray(this.infoIte.tearoomTitleImage)
					? this.infoIte.tearoomTitleImage.filter(Boolean)
					: []
				if (!urls.length) return
				uni.previewImage({
					urls,
					current: urls[index] || urls[0]
				})
			},
			// 获取详情
			getDetail() {
				let data = {
					tearoomId: this.tearoomId
				}
				this.$Request.get("/app/tearoom/selectTearoomById", data).then(res => {
					if (res.code == 0) {
						this.infoIte = res.data
						if (this.infoIte.tearoomTitleImage) {
							this.infoIte.tearoomTitleImage = this.infoIte.tearoomTitleImage.split(',')
						}
						if (this.infoIte.tearoomLabel) {
							this.infoIte.tearoomLabel = this.infoIte.tearoomLabel.split(',')
						}
						// 详情接口未返回快慢充统计时，用设备列表补齐
						this.fillChargeCounts()
					}
					uni.hideLoading()
				});
			},
			fillChargeCounts() {
				if (!this.tearoomId || !this.infoIte) return
				// 已有统计则不重复请求
				if (this.infoIte.kuaiCount != null && this.infoIte.manCount != null) return
				this.$Request.get("/app/goods/selectGoodsList", {
					tearoomId: this.tearoomId
				}).then(res => {
					if (res.code != 0 || !res.data) return
					const all = res.data || []
					const kuai = all.filter(g => g.goodsCategory == 1)
					const man = all.filter(g => g.goodsCategory == 2)
					this.$set(this.infoIte, 'kuaiCount', kuai.length)
					this.$set(this.infoIte, 'kuaiCount2', kuai.filter(g => g.goodsStatus == 2).length)
					this.$set(this.infoIte, 'manCount', man.length)
					this.$set(this.infoIte, 'manCount2', man.filter(g => g.goodsStatus == 2).length)
				})
			},
			// 获取当前价格
			getPrice() {

				let data = {
					tearoomId: this.tearoomId
				}
				this.$Request.get("/app/goodsTime/selectGoodsTimeByTime", data).then(res => {
					if (res.code == 0) {
						this.priceInfo = res.data

					}
				});
			},
			getPricelist() {
				let data = {
					tearoomId: this.tearoomId
				}

				this.$Request.get("/app/goodsTime/selectGoodsTimeList", data).then(res => {
					if (res.code == 0) {

						this.priceList = res.data
					}
				});
			},
			// 获取充电插口
			getlist() {
				let data = {
					tearoomId: this.tearoomId,
					goodsCategory: this.current == 1 ? this.carIndex == 0 ? 1 : 2 : '', // 1快充 2慢充
				}
				this.$Request.get("/app/goods/selectGoodsList", data).then(res => {
					if (res.code == 0) {
						res.data.forEach(d => {
							if (d.orders && d.status == 1 && d.goodsStatus == 2) {
								const endTimeStr = new Date(d.orders.endTime.replace(' ', 'T'));
								const startTimeStr = new Date();
								d.timeDiff = endTimeStr - startTimeStr; // 单位：毫秒
							}
						})
						this.list = res.data
						this.goodsId = this.list[0].goodsId


					}
				});
			},
			// 查看位置
			openMap() {
				let that = this
				uni.openLocation({
					latitude: that.infoIte.tearoomLatitude - 0,
					longitude: that.infoIte.tearoomLongitude - 0,
					name: that.infoIte.tearoomAddress,
					success: function() {
						console.log('success');
					}
				});

			}
		}
	}
</script>

<style lang="scss">
	page {
		background: #F7F7F7;
	}

	.bgColorTop {
		width: 100%;
		background: #F7F7F7;
		position: relative;
		padding-top: 20rpx;
	}

	.bgColorTop::before {
		content: '';
		position: absolute;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		background: linear-gradient(93deg, #E1FFF4 0%, #EBFEF6 37%, #F6FFF7 100%);
		z-index: 1;
		/* 确保在垂直渐变之上 */
	}

	.bgColorTop::after {
		content: "";
		position: absolute;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		background: linear-gradient(to bottom, transparent, #F7F7F7);
		/* 垂直渐变，结束颜色为透明 */
		z-index: 2;
		/* 确保在水平渐变之上 */
	}

	.scroll-view_H {
		white-space: nowrap;
		width: 100%;
		position: relative;
		z-index: 9;
		margin: 20rpx 0 20rpx 30rpx;

		.scroll-view-item_H {
			display: inline-block;
			width: 208rpx;
			height: 145rpx;
			margin-right: 10rpx;

			image {
				width: 100%;
				height: 100%;
				border-radius: 10rpx;
			}
		}
	}

	.addbox {
		background: #FFFFFF;
		border-radius: 24rpx;
		position: relative;
		z-index: 9;
		margin: 20rpx 30rpx;

		.addboxTitle {
			font-weight: bold;
			font-size: 32rpx;
			color: #232323;
		}

		.addboxTit {
			font-weight: 400;
			font-size: 24rpx;
			color: #999999;
			display: flex;
			align-items: center;
			margin-top: 10rpx;

			text {
				width: 1rpx;
				height: 25rpx;
				display: inline-flex;
				background: #E6E6E6;
				margin: 0 10rpx;

			}
		}

		.addboxAdd {
			height: 67rpx;
			background: #E1FFF4;
			border-radius: 0rpx 0rpx 24rpx 24rpx;
			display: flex;
			align-items: center;
			justify-content: space-between;
			padding: 0 30rpx;

			.addboxAdd_info {
				display: flex;
				align-items: center;
				font-family: Source Han Sans CN;
				font-weight: 400;
				font-size: 26rpx;
				color: #999999;

				image {
					width: 24rpx;
					height: 26rpx;
					margin-right: 10rpx;
				}
			}

			image {
				width: 38rpx;
				height: 38rpx;
			}
		}
	}

	.tabbox {
		display: flex;
		align-items: center;
		justify-content: space-between;
		margin: 0 30rpx;

		.tabboxIte {
			width: 48%;
			height: 60rpx;
			border-radius: 30rpx;
			display: flex;
			align-items: center;
			justify-content: center;
			position: relative;

			.tabboxIte_size {
				width: 35rpx;
				height: 35rpx;
				font-weight: 500;
				font-size: 26rpx;
				background: #FFFFFF;
				border-radius: 8rpx;
				display: flex;
				align-items: center;
				justify-content: center;
				margin-right: 20rpx;
			}

			.tabboxIteTit {
				display: flex;
				align-items: center;
				font-family: Source Han Sans CN;
				font-weight: 500;
				font-size: 26rpx;
				color: #FFFFFF;

				text {
					width: 1rpx;
					height: 24rpx;
					background: #FFFFFF;
					display: inline-flex;
					margin: 0 15rpx;
				}

			}

			/* 向下三角形 */
			.triangle-down {

				border-left: 16rpx solid transparent;
				border-right: 16rpx solid transparent;
				// border-top:16rpx solid #00A9EE;
				position: absolute;
				bottom: -15rpx;
			}
		}
	}

	.skubox {
		margin: 30rpx 30rpx 30rpx;
		background: #FFFFFF;
		border-radius: 24rpx;
		padding: 30rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;

		.scroll-view_HSku {
			white-space: nowrap;
			width: 100%;

		}

		.skubox_ite {
			min-width: 115rpx;
			height: 122rpx;
			background: #FFFFFF;
			border-radius: 16rpx;
			border: 1px solid #5CC477;
			display: inline-flex;
			align-items: center;
			justify-content: center;
			flex-direction: column;
			color: #5CC477;
			margin-right: 15rpx;
			padding: 0 20rpx;

			.skubox_iteNum {
				font-weight: bold;
				font-size: 32rpx;
			}

			.skubox_iteTit {
				font-weight: 500;
				font-size: 24rpx;
			}
		}

		.skubox_iteAct {
			border: 1px solid #FF130A !important;
			color: #FF130A !important;

		}

		.skubox_detail {

			display: flex;
			align-items: center;
			margin-left: 30rpx;
		}
	}

	.pricebox {
		background: #FFFFFF;
		border-radius: 24rpx;
		margin: 20rpx 30rpx;
		padding: 30rpx;

		.priceTop {
			display: flex;
			align-items: center;
			justify-content: space-between;

			.priceTop_more {
				display: flex;
				align-items: center;
				font-weight: 400;
				font-size: 24rpx;
				color: #999999;
			}

			.priceboxTitle {
				font-weight: 800;
				font-size: 30rpx;
				color: #333333;
			}
		}

		.priceboxInfo {
			background: #F5F5F5;
			border-radius: 24rpx;
			display: flex;
			margin-top: 20rpx;

			.priceboxInfo_p {
				padding: 30rpx;
				flex: 1;

				.priceboxInfo_pTit {
					font-family: PingFang SC;
					font-weight: 800;
					font-size: 24rpx;
					color: #333333;
					margin-top: 10rpx;

				}

				.priceboxInfo_pNum {
					font-weight: 800;
					font-size: 24rpx;
					color: #333333;
					letter-spacing: 2rpx;

					text {
						font-size: 38rpx;
						color: #FF3838;
					}
				}
			}

			.priceboxInfo_vip {
				padding: 30rpx;
				flex: 1;
				background: linear-gradient(91deg, #FFF0E1 0%, #FFFBF5 100%);
				border-radius: 0rpx 24rpx 24rpx 0rpx;



				.priceboxInfo_vip_pTit {
					font-weight: 800;
					font-size: 24rpx;
					color: #915230;
					margin-top: 10rpx;

				}

				.priceboxInfo_vip_pbtn {
					width: 110rpx;
					height: 36rpx;
					background: linear-gradient(90deg, #8F4D2B 0%, #49230A 100%);
					border-radius: 18rpx;
					font-family: PingFang SC;
					font-weight: 500;
					font-size: 20rpx;
					color: #FFFFFF;
					display: flex;
					align-items: center;
					justify-content: center;
				}

				.priceboxInfo_vip_pNum {
					letter-spacing: 2rpx;
					font-family: PingFang SC;
					font-weight: 800;
					font-size: 24rpx;
					color: #333333;

					text {
						font-size: 38rpx;
						color: #4A240C;
					}
				}
			}
		}
	}


	.tipbox {
		background: #FFFFFF;
		border-radius: 24rpx;
		margin: 20rpx 30rpx;
		padding: 30rpx;

		.tipboxTitle {
			font-weight: 800;
			font-size: 30rpx;
			color: #333333;
			line-height: 32rpx;
		}

		.tipboxTit {
			font-weight: 500;
			font-size: 26rpx;
			color: #999999;
			margin-top: 10rpx;
		}
	}

	.btn {
		height: 100rpx;
		background: #32BE62;
		border-radius: 50rpx;
		font-weight: 500;
		font-size: 30rpx;
		color: #FFFFFF;
		margin: 30rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		letter-spacing: 4rpx;
	}


	.Charging {
		background: #FFFFFF;
		border-radius: 24rpx;
		padding: 30rpx 20rpx 20rpx 20rpx;
		margin: 20rpx 30rpx;

		.Charging_Head {
			display: flex;
			align-items: center;
			justify-content: space-between;

			.Charging_HeadTitle {
				font-weight: 500;
				font-size: 30rpx;
				color: #232323;
			}

			.Charging_HeadTit {
				display: flex;
				align-items: center;
				font-weight: 400;
				font-size: 24rpx;
				color: #999999;
			}
		}

		.Charging_iteActs {
			color: #A2A2A2 !important;
			border: 1px solid #A2A2A2 !important;
		}

		.Charging_iteAct {
			color: #FF3838 !important;
			border: 1px solid #FF3838 !important;
		}

		.Charging_iteActl {
			background: #5CC477 !important;
			color: #FFFFFF !important;
		}

		.Charging_ite {
			min-width: 115rpx;
			padding: 0 20rpx;
			height: 122rpx;
			background: #FFFFFF;
			border-radius: 16rpx;
			border: 1px solid #5CC477;
			color: #5CC477;
			display: flex;
			align-items: center;
			// justify-content: center;
			flex-direction: column;
			margin-bottom: 20rpx;
			position: relative;
			padding-bottom: 10rpx;



			.Charging_iteNum {
				font-weight: bold;
				font-size: 38rpx;
				margin-top: 20rpx;
			}

			.Charging_iteTit {
				font-weight: 500;
				font-size: 24rpx;
			}

			.Charging_iteData {
				display: flex;
				align-items: center;
				font-family: Source Han Sans CN;
				font-weight: 400;
				font-size: 16rpx;
				color: #FF3838;
				position: absolute;
				bottom: 0rpx;
			}

			::v-deep .u-count-down__text {
				font-family: Source Han Sans CN;
				font-weight: 400;
				font-size: 16rpx;
				color: #FF3838;
			}
		}
	}

	.popuI {
		background: #FFFFFF;
		border-radius: 24rpx 24rpx 0rpx 0rpx;
		padding: 30rpx 30rpx 50rpx;


		.popuI_title {
			font-family: PingFang SC;
			font-weight: bold;
			font-size: 32rpx;
			color: #1B1B1B;
			text-align: center;
		}

		.scroll-Y {
			height: 500rpx;
		}

		.popuITop {
			display: flex;
			margin: 20rpx 0;
			justify-content: space-between;
			font-family: PingFang SC;
			font-weight: bold;
			font-size: 26rpx;
			color: #666666;
		}

		.popuItem {
			background: #F9F9F9;
			border-radius: 18rpx;
			padding: 15rpx 30rpx;
			margin: 20rpx 0 30rpx;
			display: flex;
			justify-content: space-between;
			align-items: center;
		}

		.shiduan {
			width: 40%;
			font-family: PingFang SC;
			font-weight: bold;
			font-size: 26rpx;
			color: #666666;
		}

		.type {
			width: 20%;
			text-align: center;

			.yjtit {
				font-family: PingFang SC;
				font-weight: bold;
				font-size: 26rpx;
				color: #666666;
			}

			.hyjtit {
				font-family: PingFang SC;
				font-weight: bold;
				font-size: 26rpx;
				color: #4A240C;
			}

		}

		.jiage {
			width: 40%;
			text-align: right;

			.yj {
				font-family: Source Han Sans CN;
				font-weight: 800;
				font-size: 28rpx;
				color: #222222;
			}

			.hyj {
				font-family: Source Han Sans CN;
				font-weight: 800;
				font-size: 28rpx;
				color: #4A240C;
			}
		}
	}
</style>