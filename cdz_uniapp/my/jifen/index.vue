<template>
	<view>
		<view class="flex align-center justify-between"
			style="background:#FFFFFF;padding: 40rpx 30rpx;">

			<view class="">
				<view>总积分</view>
				<view style="font-size: 50upx;font-weight: bold;flex: 6;">{{ integralNum ? integralNum : 0 }}
				</view>
				<view class="text-sm" style="margin-top: 10rpx;">{{jfTip}}</view>
			</view>

			<view v- @click="goChongZhi"
				style="background:#5CC477;color: #FFFFFF;padding: 20upx 30rpx;border-radius: 10upx;text-align: center;">
				立即兑换</view>
		</view>

		<view class="padding" style="margin-top: 20rpx;background:#FFFFFF;">
			<text style="font-size: 32upx;">积分记录</text>

			<view v-for="(item, index) in contents" :key="index"
				style="padding: 25upx 0;border-bottom: 1upx solid #424242;font-size: 32upx;color: #666666;">
				<view class="flex align-center justify-between">
					<view class="flex-sub" style="font-size: 28rpx;">
						{{item.content}}
					</view>
					<view class="jifen_number" v-if="item.type==1">+ {{ item.num }}</view>
					<view class="jifen_number" v-if="item.type==2">- {{ item.num }}</view>
				</view>
				<view style="color: #CCCCCC;font-size: 25rpx;">{{ item.createTime }}</view>
			</view>
			<empty v-if="contents.length == 0" content="暂无记录" />
		</view>
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
				integralNum: 0,
				page: 1,
				limit: 10,
				pages: 1,
				contents: [],
				jfTip: ''
			};
		},
		onShow() {
			this.getUserInfo()
			this.getlist()
			// 总积分下提示语	854
			this.$Request.getT('/app/common/type/854').then(res => {
				if (res.code == 0) {
					if (res.data && res.data.value) {
						this.jfTip = res.data.value
					}
				}
			});
		},
		methods: {
			goChongZhi() {
				uni.navigateTo({
					url: '/my/jifen/Mall'
				})
			},
			//获取个人积分
			getUserInfo() {
				this.$Request.getT("/app/integral/selectByUserId").then(res => {
					if (res.code == 0) {
						this.integralNum = res.data.integralNum

					}
				});
			},
			// 积分明细
			getlist() {
				let data = {
					page: this.page,
					limit: this.limit,
				}
				this.$Request.getT("/app/integral/details", data).then(res => {
					if (res.code === 0) {
						this.pages = res.data.pages
						if (this.page === 1) {
							this.contents = res.data.records;
						} else {
							this.contents = [...this.contents, ...res.data.records];
						}
					}
					uni.stopPullDownRefresh();
					uni.hideLoading();
				});
			}
		},
		onReachBottom: function() {
			if (this.pages > this.page) {
				this.page = this.page + 1;
				this.getlist();
			} else {
				uni.showToast({
					title: '已经到底了~',
					icon: 'none'
				})
			}
		},
		onPullDownRefresh: function() {
			this.page = 1;
			this.getlist();
		}
	};
</script>

<style scoped>
	page {
		background: #F5F5F5;
	}



	.jifen_number {
		color: #41B458;
		margin-left: 100rpx;
		padding-top: 10rpx;
	}
</style>