<template>
	<view class="padding-bottom-xl">
		<u-sticky customNavHeight="0">
			<view class="search-box">
				<u-search style="width: 100%;" placeholder="搜索充电位名称" :focus="false" v-model="keyword"
					:show-action="true" :animation="true" shape="square" bg-color="#F7F7F7" color="#1A1A1A"
					action-text="搜索" @search="doSearch()" @custom="doSearch()"></u-search>
			</view>

			<view class="bxstate">
				<view class="bxstate_view">
					<view @tap="bxStateSel(1)" :class="current == 1 ? 'bxstate_selitem' : 'bxstate_item'">营业中</view>
					<view @tap="bxStateSel(2)" :class="current == 2 ? 'bxstate_selitem' : 'bxstate_item'">停止营业</view>
				</view>
			</view>
		</u-sticky>
		<view class="listItem" v-for="(item,index) in list" v-if="list.length > 0">
			<view class="flex padding">
				<image :src="item.goodsCoverImage?item.goodsCoverImage:'../../static/logo.png'" class="listItem_img">
				</image>
				<view class="listItem_right flex justify-center" style="flex-direction: column;">
					<view class="right_bxname flex align-center justify-between">
						{{item.goodsName}}
						<view style="color: green;" class="right_bxname-txt" v-if="item.useState === 1">
							空闲
						</view>
						<view style="color: red;" class="right_bxname-txt" v-if="item.useState === 2">
							占用
						</view>
					</view>
					<view class="right_bxcrat">创建时间：<text>{{item.createTime}}</text></view>
					<!-- <view class="right_bxcrat">设备：房门/电源</view> -->
					<!-- <view class="right_bxmoney">￥<text>{{item.price}}</text></view> -->
				</view>
			</view>
			<u-line direction="row" color="#E6E6E6" border-style="dashed" />
			<view class="flex justify-end margin-top-sm">
				<view class="btn1 flex align-center justify-center" @click="xiajia(item)" v-if="item.status == 1">下架
				</view>
				<view class="btn1 flex align-center justify-center" @click="shangjia(item)" v-else>上架</view>
				<!-- <view style="background-color: #32BE62;color: #ffffff;" class="btn1 flex align-center justify-center"
					@click="lightControl(item,1)">开灯</view>
				<view style="border-color: #999999;color: #999999;" class="btn1 flex align-center justify-center"
					@click="lightControl(item,2)">关灯</view> -->
				<!-- <view class="btn">查看详情</view> -->
			</view>
		</view>
		<u-loadmore v-if="list.length >0" marginTop="20" :status="status" />
		<empty v-if="list.length == 0" />
	</view>
</template>

<script>
	import empty from '@/components/empty.vue'
	export default {
		components: {
			empty
		},
		data() {
			return {
				list: [],
				current: 1,
				keyword: '',
				page: 1,
				pages: 1,
				limit: 10,
				status: 'loadmore',
			}
		},
		onLoad(e) {
			if (e.tearoomId) {
				this.tearoomId = e.tearoomId
				this.getRoomList()
			}

		},
		onPullDownRefresh() {
			this.page = 1
			this.getRoomList()
		},
		onReachBottom() {
			if (this.page < this.pages) {
				this.page += 1
				this.status = 'loading'
				this.getRoomList()
			}
		},
		methods: {
			lightControl(item, type) {
				uni.showModal({
					title: '提示',
					content: type === 1 ? '确认开灯吗？' : '确认关灯吗？',
					complete: (ret) => {
						if (ret.confirm) {
							let data = {
								goodsId: item.goodsId,
								status: type
							}
							uni.showLoading({
								title: '加载中...'
							})
							this.$Request.getT('/app/room/lightControl', data).then(res => {
								uni.hideLoading()
								if (res.code == 0) {
									uni.showToast({
										title: type === 1 ? '已开灯' : '已关灯'
									})
									this.page = 1
									this.getRoomList()
								} else {
									uni.showToast({
										title: res.msg,
										icon: 'none'
									})
								}
							})
						}
					}
				})
			},
			//上架
			shangjia(item) {
				let that = this
				uni.showModal({
					title: '提示',
					content: '确认上架吗？',
					complete(ret) {
						if (ret.confirm) {
							uni.showLoading({
								title: '加载中...'
							})
							let data = {
								goodsId: item.goodsId,
								status: 1
							}
							that.$Request.postJson('/app/goods/updateGoods', data).then(res => {
								if (res.code == 0) {
									uni.hideLoading()
									uni.showToast({
										title: '已上架'
									})
									that.page = 1
									that.getRoomList()
								} else {
									uni.hideLoading()
									uni.showToast({
										title: res.msg,
										icon: 'none'
									})
								}
							})
						}
					}
				})
			},
			//下架
			xiajia(item) {
				let that = this
				uni.showModal({
					title: '提示',
					content: '确认下架吗？',
					complete(ret) {
						if (ret.confirm) {
							uni.showLoading({
								title: '加载中...'
							})
							let data = {
								goodsId: item.goodsId,
								status: 2
							}
							that.$Request.postJson('/app/goods/updateGoods', data).then(res => {
								if (res.code == 0) {
									uni.hideLoading()
									uni.showToast({
										title: '已下架'
									})
									that.page = 1
									that.getRoomList()
								} else {
									uni.hideLoading()
									uni.showToast({
										title: res.msg,
										icon: 'none'
									})
								}
							})
						}
					}
				})
			},
			//获取列表
			getRoomList() {
				let data = {
					page: this.pages,
					limit: this.limit,
					goodsName: this.keyword,
					status: this.current,
					tearoomId: this.tearoomId
				}
				this.$Request.getT('/app/goods/selectGoodsList', data).then(res => {
					uni.stopPullDownRefresh()
					if (res.code == 0) {
						this.pages = res.data.totalPage
						if (this.page < this.pages) {
							this.status = 'loadmore'
						} else {
							this.status = 'nomore'
						}
						if (this.page == 1) {
							this.list = res.data.list
						} else {
							this.list = [...this.list, ...res.data.list]
						}
					} else {
						uni.showToast({
							title: res.msg,
							icon: 'none'
						})
					}
				})
			},
			doSearch(keyword) {
				this.page = 1
				this.getRoomList()
			},
			bxStateSel(index) {
				this.current = index;
				this.page = 1
				this.getRoomList()
			}
		}
	}
</script>

<style lang="scss">
	page {
		background: #F7F7F7;
	}

	.listItem {
		width: 686rpx;
		// height: 88rpx;
		background: #FFFFFF;
		border-radius: 24rpx;
		margin: 0 auto;
		margin-top: 20rpx;
		padding-bottom: 20rpx;


		.btn {
			width: 150rpx;
			height: 60rpx;
			background: #442916;
			border-radius: 30rpx;
			margin-right: 30rpx;
			text-align: center;
			line-height: 60rpx;
			font-size: 24rpx;
			font-family: PingFang SC;
			font-weight: 500;
			color: #FFFFFF;
		}

		.btn1 {
			width: 150rpx;
			height: 60rpx;
			border: 1px solid #32BE62;
			border-radius: 30rpx;
			margin-right: 30rpx;
			text-align: center;
			font-size: 24rpx;
			font-family: PingFang SC;
			font-weight: 500;
			color: #32BE62;
		}

		.listItem_img {
			width: 170rpx;
			height: 170rpx;
			border-radius: 24rpx;
		}

		.listItem_right {
			width: 430rpx;
			margin-left: 34rpx;

			.right_bxname {
				font-size: 32rpx;
				font-family: PingFang SC;
				font-weight: 800;
				color: #333333;
			}

			.right_bxname-txt {
				font-size: 26rpx;
				font-weight: normal;
			}

			.right_bxmoney {
				font-size: 24rpx;
				font-family: PingFang SC;
				font-weight: 500;
				color: #333333;
				margin-top: 10rpx;

				text {
					font-size: 32rpx;
				}
			}

			.right_bxcrat {
				font-size: 24rpx;
				font-family: PingFang SC;
				font-weight: 500;
				color: #999999;
				margin-top: 10rpx;

				text {
					color: #333333;
				}
			}
		}
	}

	.bxstate {
		width: 750rpx;
		height: 120rpx;
		background: #F7F7F7;
		padding-top: 20rpx;


		.bxstate_view {
			width: 686rpx;
			height: 88rpx;
			background: #FFFFFF;
			border-radius: 24rpx;
			display: flex;
			margin: 0 auto;
			text-align: center;
			font-size: 28rpx;
			font-family: PingFang SC;
			font-weight: bold;
			color: #0A0E10;

			.bxstate_item {
				width: 333rpx;
				height: 88rpx;
				display: flex;
				align-items: center;
				justify-content: center;
			}

			.bxstate_selitem {
				width: 333rpx;
				height: 88rpx;
				background: rgba(50, 190, 98, 0.2);
				border-radius: 24rpx;
				color: #32BE62 !important;
				display: flex;
				align-items: center;
				justify-content: center;
			}
		}
	}

	.search-box {
		width: 100%;
		/* background-color: rgb(242, 242, 242); */
		padding: 15upx 2.5%;
		display: flex;
		justify-content: space-between;
		background-color: #FFFFFF;
	}
</style>