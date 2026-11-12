<template>
	<view class="" :style="isTrue?'padding-bottom: 200rpx;':''">
		<!-- 建议放在外层 -->
		<u-sticky customNavHeight="0">
			<view class="HeadTop">
				<view class="HeadTop_maps"  @click="goSelectCity">
					<image src="/static/images/index/place.png" mode="widthFix"></image>
					<text>{{city}}</text>
				</view>
				<view class="HeadTop_search">
					<u-search placeholder="搜索附近充电站" searchIconColor="#333333" v-model="keyword" @change="changeSearch"
						:showAction="false"></u-search>
				</view>
				<view class="HeadTop_map" v-if="isTrue" @click="isTrue = !isTrue">
					<image src="/static/images/index/mapIcon.png"></image>
					<text>地图</text>
				</view>
				<view class="HeadTop_map" v-else @click="isTrue = !isTrue">
					<image src="/static/images/index/list.png"></image>
					<text>列表</text>
				</view>

			</view>
			<view class="bg" v-if="!isTrue">
				<u-tabs :list="listTab" @click="clickTab" :scrollable="false" lineColor="#5CC477"
					:activeStyle="activeStyle" :inactiveStyle="inactiveStyle" :current="current"></u-tabs>
			</view>
		</u-sticky>

		<block v-if="isTrue">
			<!-- banner -->
			<view class="bannerBox">
				<swiper class="swiper" :autoplay="true" interval="2000" duration="500" :circular="true"
					style="width: 100%;height: 250rpx;border-radius: 24rpx;">
					<swiper-item v-for="(item,indexb) in BannerList" :key='indexb' @click="goNav(item.url)">
						<image :src="item.imageUrl" mode="aspectFill"
							style="width: 100%;height: 250rpx;border-radius: 24rpx;"></image>
					</swiper-item>
				</swiper>
			</view>

			<view class="">
				<u-tabs :list="listTab" @click="clickTab" :scrollable="false" lineColor="#5CC477"
					:activeStyle="activeStyle" :inactiveStyle="inactiveStyle" :current="current"></u-tabs>
			</view>

			<view class="" v-if="current==0">
				<view class="carbox" v-for="(item,index) in list" :key="index" @click="goDetail(item)">
					<view class="carboxInfo">
						<view class="carboxInfo_title">{{item.tearoomName}}</view>
						<view class="carboxInfo_add">{{item.tearoomAddress}}</view>
						<view class="carboxInfo_tip">
							<view class="carboxInfo_tip_kxcz">空闲{{item.manCount-item.manCount2}}个插座</view>
							<text></text>
							<view class="carboxInfo_tip_cz">共{{item.manCount}}个插座</view>
						</view>
					</view>
					<view class="carbox_km">
						<image src="/static/images/index/lcIcon.png"></image>
						<view>{{item.distance}}</view>
					</view>
				</view>


			</view>

			<view class="" v-if="current==1">
				<view class="typebox">
					<view class="typeboxIte" :class="typeIndex==indexT?'typeboxAct':''" @click="bindType(indexT)"
						v-for="(item,indexT) in listType" :key="indexT">
						{{item.name}}
					</view>
				</view>

				<view class="carsbox" v-for="(item,index) in list" :key="index" @click="goDetail(item)">
					<view class="carsboxTop">
						<view class="carsboxTop_name">{{item.tearoomName}}</view>
						<view class="carsboxTop_add">
							<image src="/static/images/index/add.png"></image>
							<text>{{item.distance}}</text>
						</view>
					</view>

					<view class="carsboxTit">
						<view v-for="(ite,ind) in item.tearoomLabel" :key="ind">
							{{ite}}
							<text v-if="item.tearoomLabel.length - 1>ind"></text>
						</view>
					</view>

					<view class="carsboxLabel" v-if="item.welfareContent">
						<image src="/static/images/index/tc.png"></image>
						{{item.welfareContent}}
					</view>
					<view class="carsboxLine"></view>
					<view class="carsbox_bot">
						<view class="carsbox_botPrice">
							￥<text>{{item.price}}</text>/度
						</view>

						<view class="carsbox_botSku">
							<view class="carsbox_botSku_kuai">
								<view class="carsbox_botSku_kuai_tit">快</view>
								<view class="carsbox_botSku_kuai_num">
									闲{{item.kuaiCount - item.kuaiCount2}}/<text>{{item.kuaiCount}}</text>
								</view>
							</view>
							<view class="carsbox_botSku_man margin-left-xs">
								<view class="carsbox_botSku_man_tit">慢</view>
								<view class="carsbox_botSku_man_num">
									闲{{item.manCount-item.manCount2}}/<text>{{item.manCount}}</text>
								</view>
							</view>
						</view>
					</view>
				</view>


			</view>
			<u-loadmore :status="status" v-if="list.length!=0" />
			<empty v-if="list.length==0"></empty>
		</block>
		<block v-else>
			<view class="page-body">
				<view class="page-section page-section-gap">
					<map id="map" style="width: 100%; " :style="'height:'+screenHeight+'px'" :latitude="latitude"
						:longitude="longitude" :markers="covers" @markertap="handleMarkerTap"
						@callouttap="handleMarkerTap">
					</map>
				</view>
			</view>

			<cover-view class="dwIcon" @click="getMyAdd()">
				<cover-image src="/static/images/index/dw.png"></cover-image>
			</cover-view>

			<cover-view class="carbox dw" v-if="current==0" @click="goDetail(listInfo)">
				<cover-view class="carboxInfo">
					<cover-view class="carboxInfo_title">{{listInfo.tearoomName}}</cover-view>
					<cover-view class="carboxInfo_add">{{listInfo.tearoomAddress}}</cover-view>
					<cover-view class="carboxInfo_tip">
						<cover-view
							class="carboxInfo_tip_kxcz">空闲{{listInfo.manCount-listInfo.manCount2}}个插座</cover-view>
						<text></text>
						<cover-view class="carboxInfo_tip_cz">共{{listInfo.manCount}}个插座</cover-view>
					</cover-view>
				</cover-view>
				<cover-view class="carbox_km">
					<image src="/static/images/index/lcIcon.png"></image <cover-view>{{listInfo.distance}}
				</cover-view>
			</cover-view>
			</cover-view>

			<view class="carsbox dw" v-if="current==1" @click="goDetail(listInfo)">
				<view class="carsboxTop">
					<view class="carsboxTop_name">{{listInfo.tearoomName}}</view>
					<view class="carsboxTop_add">
						<image src="/static/images/index/add.png"></image>
						<text>{{listInfo.distance}}</text>
					</view>
				</view>

				<view class="carsboxTit">
					<view v-for="(ite,ind) in listInfo.tearoomLabel" :key="ind">
						{{ite}}
						<text v-if="listInfo.tearoomLabel.length - 1>ind"></text>
					</view>
				</view>

				<view class="carsboxLabel" v-if="listInfo.welfareContent">
					<image src="/static/images/index/tc.png"></image>
					{{listInfo.welfareContent}}
				</view>
				<view class="carsboxLine"></view>
				<view class="carsbox_bot">
					<view class="carsbox_botPrice">
						￥<text>{{listInfo.price}}</text>/度
					</view>

					<view class="carsbox_botSku">
						<view class="carsbox_botSku_kuai">
							<view class="carsbox_botSku_kuai_tit">快</view>
							<view class="carsbox_botSku_kuai_num">
								闲{{listInfo.kuaiCount-listInfo.kuaiCount2}}/<text>{{listInfo.kuaiCount}}</text>
							</view>
						</view>
						<view class="carsbox_botSku_man margin-left-xs">
							<view class="carsbox_botSku_man_tit">慢</view>
							<view class="carsbox_botSku_man_num">
								闲{{listInfo.manCount-listInfo.manCount2}}/<text>{{listInfo.manCount}}</text>
							</view>
						</view>
					</view>
				</view>
			</view>



		</block>
		<myTabbar :value="0" />
	</view>
</template>

<script>
	import myTabbar from '@/components/myTabbar.vue'
	import empty from '@/components/empty.vue'
	export default {
		components: {
			myTabbar,
			empty
		},
		data() {
			return {
				keyword: '',
				BannerList: [],
				listTab: [{
					id: 1,
					name: '电瓶车',
				}, {
					id: 2,
					name: '汽车桩',
				}],
				current: 0,
				activeStyle: {
					'fontWeight': '800',
					'fontSize': '28rpx',
					'color': ' #5CC477',
				},
				inactiveStyle: {
					'fontWeight': '800',
					'fontSize': '28rpx',
					'color': ' #333333',
				},
				status: 'loadmore',

				listType: [{
					id: 1,
					name: '全部'
				}, {
					id: 2,
					name: '快充'
				}, {
					id: 3,
					name: '慢充'
				}, {
					id: 4,
					name: '距离最近'
				}, {
					id: 5,
					name: '低价优先'
				}],
				typeIndex: 0,

				isTrue: true,
				latitude: '',
				longitude: '',
				city: '',
				list: [],
				page: 1,
				pages: 1,

				covers: [],
				screenHeight: '',

				listInfo: {}, //地图展示数据
			}
		},
		onLoad() {
			//获取手机系统信息
			let systemInfo = uni.getSystemInfoSync();
			console.log(systemInfo)
			this.screenHeight = systemInfo.screenHeight - 200
			
		},
		onShow() {
			this.city = uni.getStorageSync('city')
			this.getBannerImage()
			//获取城市
			let that = this
			if (uni.getStorageSync('longitude') && uni.getStorageSync('latitude')) {
				that.latitude = uni.getStorageSync('latitude')
				that.longitude = uni.getStorageSync('longitude')
				that.selectCity(that.longitude, that.latitude);
			} else {
				uni.getLocation({
					type: 'gcj02', //wgs84  gcj02
					success: function(res) {
						console.log('获取地址', res)
						//根据定位的经纬度获取城市名称
						that.latitude = res.latitude
						that.longitude = res.longitude
						uni.setStorageSync('latitude', res.latitude)
						uni.setStorageSync('longitude', res.longitude)
						that.selectCity(that.longitude, that.latitude);
					},
					fail: function() {
						console.log('获取地址失败')
					}
				})
			}
			
		},
		methods: {
			// 选择城市
			goSelectCity() {
				// uni.navigateTo({
				// 	url: '/pages/index/citys/citys'
				// });
				let that = this
				uni.chooseLocation({
					success: function(res) {
						console.log('位置名称：' + res.name);
						console.log('详细地址：' + res.address);
						console.log('纬度：' + res.latitude);
						console.log('经度：' + res.longitude);
						that.longitude = res.longitude
						that.latitude = res.latitude
						uni.setStorageSync('latitude', res.latitude)
						uni.setStorageSync('longitude', res.longitude)
						that.selectCity(that.longitude, that.latitude);
					}
				});
			},
			//获取banner图
			getBannerImage() {
				let data = {
					classify: 1
				}
				this.$Request.getT("/app/banner/selectBannerList", data).then(res => {
					if (res.code == 0) {
						if (res && res.code == 0) {
							this.BannerList = res.data
						}
					}
				});

			},
			goSearch() {
				if (uni.getStorageSync('token')) {
					uni.navigateTo({
						url: '/my/search/index'
					})
				} else {
					uni.navigateTo({
						url: '/pages/public/login'
					})
				}
			},
			// 点击地图获取当前位置 移动到当前位置
			getMyAdd() {
				let that = this
				uni.getLocation({
					type: 'gcj02', //wgs84  gcj02
					success: function(res) {
						console.log('获取地址', res)
						//根据定位的经纬度获取城市名称
						that.latitude = res.latitude
						that.longitude = res.longitude
						uni.setStorageSync('latitude', res.latitude)
						uni.setStorageSync('longitude', res.longitude)

						// 在更新之前检查covers数组
						if (that.covers.length === 0) {
							that.covers = [{
								latitude: res.latitude,
								longitude: res.longitude,
								width: 36,
								height: 36,
								id: 0,
								iconPath: '/static/images/guizi.png',
							}]
						} else {
							const newCovers = [...that.covers]
							newCovers[0] = {
								latitude: res.latitude,
								longitude: res.longitude,
								width: 36,
								height: 36,
								id: 0,
								iconPath: '/static/images/guizi.png',
							}
							that.covers = newCovers
						}

						// 在更新covers后，如果需要强制刷新地图
						setTimeout(() => {
							// 获取 map 上下文，注意地图组件的 id
							const mapContext = uni.createMapContext('map', that)
							mapContext.moveToLocation({
								latitude: res.latitude,
								longitude: res.longitude
							}) // 移动到指定位置
						}, 300)
					},
					fail: function() {
						console.log('获取地址失败')
						uni.showToast({
							title: '获取位置失败',
							icon: 'none'
						})
					}
				})
			},
			//根据经纬度获取城市地址
			selectCity(longitude, latitude) {
				let that = this
				let data = {
					lat: latitude,
					lng: longitude
				}

				that.$Request.get("/app/Login/selectCity", data).then(res => {
					if (res.code == 0) {
						let cover = {
							latitude: latitude,
							longitude: longitude,
							width: 36,
							height: 36,
							id: 0,
							iconPath: '/static/images/guizi.png',

						}
						that.covers.push(cover)

						uni.setStorageSync('city', res.data.city)
						that.city = res.data.city
						that.page = 1;
						that.getList();

					}
				});

			},
			// 跳转详情
			goDetail(ite) {
				if (uni.getStorageSync('token')) {
					uni.navigateTo({
						url: '/pages/index/detail?current=' + this.current + '&tearoomId=' + ite.tearoomId
					})
				} else {
					uni.navigateTo({
						url: '/pages/public/login'
					})
				}
			},
			// 汽车筛选
			bindType(index) {
				this.typeIndex = index
				this.page = 1
				this.getList()
			},
			// 汽车桩筛选切换
			clickTab(ite) {
				this.current = ite.index
				this.page = 1
				this.getList()
			},
			// 地图点击切换
			handleMarkerTap(e) {
				console.log(e)
				// 1. 获取被点击的 markerId
				const clickedMarkerId = e.markerId || e.detail.markerId
				// 2. 根据ID找到对应的充电站数据，并更新底部的 listInfo
				this.list.forEach((d, index) => {
					if (d.tearoomId == clickedMarkerId) {
						this.listInfo = d
					}
				})
				// 3. 关键：创建一个全新的 covers 数组，确保视图更新
				const newCovers = this.covers.map(item => {
					// 判断当前标记是否为被点击的标记
					if (item.id == clickedMarkerId) {
						// 返回一个新的对象，而不是修改原对象
						return {
							...item, // 保留原有属性
							iconPath: '/static/images/index/cdiconAct.png', // 修改图标
							width: 36, // 保持宽度
							height: 50 // 增加高度以突出显示
						}
					} else {
						// 其他标记恢复默认状态
						return {
							...item,
							iconPath: '/static/images/index/cdicon.png',
							width: 36,
							height: 36
						}
					}
				})

				// 4. 将新数组赋值给 covers，触发响应式更新
				this.covers = newCovers

			},
			// 搜索
			changeSearch(e) {
				console.log(e)
				this.keyword = e
				this.page = 1
				this.getList()
			},
			// 获取列表
			getList() {
				let tearoomType = this.listTab[this.current].id // 1.电瓶车 2.汽车桩
				let type = this.listType[this.typeIndex].id

				let data = {
					status: 1,
					sort: tearoomType == 1 ? '' : type,
					page: this.page,
					limit: 10,
					latitude: this.latitude,
					longitude: this.longitude,
					tearoomType: tearoomType, //充电类型 1电瓶车 2汽车桩
					city: this.city,
					search: this.keyword,
					byUserId: uni.getStorageSync('userId')
				}
				this.$Request.getT('/app/tearoom/selectTearoomList', data).then(res => {
					uni.hideLoading();
					uni.stopPullDownRefresh();
					if (res.code == 0) {
						// 先清空除了用户位置之外的所有标记
						this.covers = this.covers.filter(item => item.id === 0)


						res.data.list.forEach((d, index) => {
							// 检查数据有效性
							if (!d) return

							if (d.distance > 1000) {
								d.distance = (d.distance / 1000).toFixed(1) + 'km'
							} else {
								d.distance = (d.distance).toFixed(1) + 'm'
							}
							if (d.tearoomLabel) {
								d.tearoomLabel = d.tearoomLabel.split(',')
							}
							let contentTip = ''
							if (this.current == 0) {
								contentTip = '空闲' + parseInt(d.manCount - d.manCount2) + '个'
							} else {
								contentTip = '快充空闲' + parseInt(d.kuaiCount - d.kuaiCount2) + '个，慢充空闲' +
									parseInt(d.manCount - d.manCount2) + '个'
							}

							let cover = {
								latitude: d.tearoomLatitude,
								longitude: d.tearoomLongitude,
								width: 36,
								height: 36,
								id: d.tearoomId,
								iconPath: '/static/images/index/cdicon.png',
								callout: { //自定义标记点上方的气泡窗口 点击有效
									content: contentTip, //文本
									color: '#FFFFFF', //文字颜色
									fontSize: 10, //文本大小
									padding: 10, //附近留白
									borderRadius: 6, //边框圆角
									bgColor: '#1EB05F', //背景颜色
									display: 'ALWAYS', //常显
								}
							}
							this.covers.push(cover)


						});
						// 设置默认选中第一个充电站标记（索引1，因为索引0是用户位置）
						if (this.covers.length > 1) {
							const newCovers = [...this.covers]
							if (newCovers[1]) {
								// 更新地图中心点
								this.latitude = newCovers[1].latitude
								this.longitude = newCovers[1].longitude

								// 高亮第一个充电站
								newCovers[1] = {
									...newCovers[1],
									iconPath: '/static/images/index/cdiconAct.png',
									height: 50
								}
								this.covers = newCovers

								// 设置默认显示的信息
								if (this.list.length > 0) {
									this.listInfo = this.list[0]
								}
							}
						}

						this.pages = res.data.totalPage
						if (this.page == 1) {
							this.list = res.data.list
						} else {
							this.list = [...this.list, ...res.data.list]
						}
						if (this.pages > this.page) {
							this.status = 'loading'
						} else {
							this.status = 'nomore'
						}
						if (this.list.length != 0) {
							this.listInfo = this.list[0]

						}
					}
				});
			}
		},
		onPullDownRefresh() {
			this.page = 1;
			this.getList();
		},
		onReachBottom() {
			if (this.pages > this.page) {
				this.page = this.page + 1
				this.getList()
			} else {
				uni.showToast({
					title: '已经到底了~',
					icon: 'none'
				})
			}

		}
	}
</script>

<style lang="scss">
	.bg {
		background: #FFFFFF;
	}

	.HeadTop {
		background: #FFFFFF;
		padding: 20rpx 30rpx;
		display: flex;
		align-items: center;

		.HeadTop_maps {
			display: flex;
			align-items: center;
			image {
				width: 30rpx;
				height:32rpx;
			}

			text {
				font-family: PingFang SC;
				font-weight: bold;
				font-size: 24rpx;
				color: #1A1A1A;
				margin-left: 10rpx;
			}
		}

		.HeadTop_map {
			width: 130rpx;
			height: 59rpx;
			background: #F7F7F7;
			border-radius: 30rpx;
			display: flex;
			align-items: center;

			image {
				width: 50rpx;
				height: 50rpx;
			}

			text {
				font-family: PingFang SC;
				font-weight: bold;
				font-size: 24rpx;
				color: #1A1A1A;
				margin-left: 10rpx;
			}
		}

		.HeadTop_search {
			flex: 1;
			margin: 0 30rpx;
		}
	}

	.bannerBox {
		margin: 20rpx 30rpx;
	}

	.carbox {
		margin: 20rpx 30rpx;
		background: #FFFFFF;
		border-radius: 24rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 30rpx;


		.carboxInfo {
			flex: 1;

			.carboxInfo_title {
				font-family: Microsoft YaHei;
				font-weight: bold;
				font-size: 32rpx;
				color: #232323;
			}

			.carboxInfo_add {
				font-weight: 400;
				font-size: 26rpx;
				color: #232323;
				margin: 15rpx 0;
			}

			.carboxInfo_tip {
				display: flex;
				align-items: center;

				text {
					width: 1rpx;
					height: 25rpx;
					background: #E6E6E6;
					margin: 0 10rpx;
				}

				.carboxInfo_tip_kxcz {
					font-weight: 400;
					font-size: 24rpx;
					color: #FF9345;
				}

				.carboxInfo_tip_cz {
					font-weight: 400;
					font-size: 24rpx;
					color: #272727;
				}
			}
		}

		.carbox_km {
			text-align: center;

			image {
				width: 70rpx;
				height: 70rpx;
			}

			text {
				font-family: Source Han Sans CN;
				font-weight: 400;
				font-size: 24rpx;
				color: #232323;
			}
		}
	}

	.typebox {
		display: flex;
		justify-content: space-between;
		padding: 15rpx 30rpx;

		.typeboxIte {
			padding: 8rpx 30rpx;
			background: #FFFFFF;
			border-radius: 55rpx;
			border: 1px solid #CCCCCC;
			font-size: 24rpx;
			color: #999999;
		}

		.typeboxAct {
			border: 1px solid #5CC477;
			color: #5CC477;
		}
	}

	.carsbox {
		margin: 20rpx 30rpx;
		background: #FFFFFF;
		border-radius: 24rpx;
		padding: 30rpx;

		.carsboxTop {
			display: flex;
			align-items: center;
			justify-content: space-between;

			.carsboxTop_name {
				font-family: Microsoft YaHei;
				font-weight: bold;
				font-size: 32rpx;
				color: #232323;
			}

			.carsboxTop_add {
				display: flex;
				align-items: center;
				padding: 4rpx 10rpx;
				border-radius: 55rpx;
				border: 1px solid #999999;

				image {
					width: 25rpx;
					height: 27rpx;
				}

				text {
					font-family: Source Han Sans CN;
					font-weight: 400;
					font-size: 24rpx;
					color: #999999;
					margin-left: 6rpx;
				}
			}
		}

		.carsboxTit {
			font-weight: 400;
			font-size: 24rpx;
			color: #999999;
			display: flex;
			align-items: center;
			margin: 15rpx 0;

			text {
				display: inline-block;
				width: 1rpx;
				height: 22rpx;
				background-color: #E6E6E6;
				margin: 0 10rpx;
			}
		}

		.carsboxLabel {
			font-weight: 400;
			font-size: 21rpx;
			color: #999999;
			background: #F5F5F5;
			border-radius: 12rpx;
			display: inline-flex;
			align-items: center;
			padding: 6rpx 30rpx 6rpx 20rpx;

			image {
				width: 24rpx;
				height: 29rpx;
				margin-right: 10rpx;
			}

		}

		.carsboxLine {
			width: 623rpx;
			height: 1rpx;
			background: #E5E5E5;
			margin: 20rpx 0;
		}

		.carsbox_bot {
			display: flex;
			align-items: center;
			justify-content: space-between;

			.carsbox_botPrice {
				font-size: 24rpx;
				color: #FF823F;

				text {
					font-weight: bold;
					font-size: 38rpx;
				}
			}

			.carsbox_botSku {
				display: flex;

				.carsbox_botSku_kuai {
					height: 42rpx;
					background: #FFFFFF;
					border-radius: 10rpx;
					border: 1px solid #FF823F;
					display: flex;

					.carsbox_botSku_kuai_tit {
						width: 40rpx;
						height: 40rpx;
						background: #FF823F;
						border-radius: 8rpx;
						border: 1px solid #FF823F;

						font-weight: 400;
						font-size: 24rpx;
						color: #FFFFFF;
						display: flex;
						align-items: center;
						justify-content: center;
					}

					.carsbox_botSku_kuai_num {
						font-weight: 400;
						font-size: 24rpx;
						color: #FF9345;
						padding: 0 15rpx;
						letter-spacing: 2rpx;
						display: flex;
						align-items: center;
						justify-content: center;

						text {
							color: #2C2C2C;
						}
					}
				}

				.carsbox_botSku_man {
					height: 42rpx;
					background: #FFFFFF;
					border-radius: 10rpx;
					border: 1px solid #5CC477;
					display: flex;

					.carsbox_botSku_man_tit {
						width: 40rpx;
						height: 40rpx;
						background: #5CC477;
						border-radius: 8rpx;
						border: 1px solid #5CC477;

						font-weight: 400;
						font-size: 24rpx;
						color: #FFFFFF;
						display: flex;
						align-items: center;
						justify-content: center;
					}

					.carsbox_botSku_man_num {
						font-weight: 400;
						font-size: 24rpx;
						color: #5CC477;
						padding: 4rpx 15rpx;
						letter-spacing: 2rpx;
						display: flex;
						align-items: center;
						justify-content: center;


						text {
							color: #2C2C2C;
						}
					}
				}
			}
		}
	}

	.dwIcon {
		position: fixed;
		top: 300rpx;
		right: 20rpx;

		cover-image {
			width: 94rpx;
			height: 94rpx;
		}
	}

	.dw {
		position: fixed;
		bottom: 160rpx;
		left: 0;
		right: 0;
	}
</style>