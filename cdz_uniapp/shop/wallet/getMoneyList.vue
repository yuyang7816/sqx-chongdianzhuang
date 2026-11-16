<template>
	<view style="text-align: left;padding-bottom: 10rpx;">
		<view v-for="(item, index) in list" :key="index" class="item">
			<view>
				<view style="color: #999999;font-size: 28upx;">
					<view style="margin-bottom: 8upx"> {{item.title}}</view>
					<view style="margin-bottom: 8upx"> {{item.content}}</view>
					<view style="margin-bottom: 8upx"> 到账时间：{{item.createTime}}</view>
					<view style="margin-bottom: 8upx;text-align: right;">
						<text style="color: #FF6F1B;font-size: 32upx;font-weight: 600"> ￥{{item.money}}</text>
					</view>
				</view>

			</view>
		</view>
		<empty v-if="list.length === 0" content="暂无提现记录" show="false"></empty>

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
				page: 1,
				limit: 10,
				pages: 1
			}
		},
		onLoad: function(e) {
			this.$queue.showLoading("加载中...");
			this.getMoney();
		},
		methods: {
			getMoney() {
				let that = this;
				let token = that.$queue.getData("token");
				if (token) {
					//可以提现金额查询预估收入查询
					let data = {
						page: that.page,
						limit: that.limit,
						shopUserId: uni.getStorageSync('userId')
					}
					that.$Request.getT("/app/userMoney/queryShopMoneyDetails", data).then(res => {
						if (res.code === 0 && res.data) {
							this.pages = res.data.pages
							if (this.page == 1) {
								that.list = res.data.records;
							} else {
								that.list = [...that.list, ...res.data.records]
							}
						}
						uni.stopPullDownRefresh();
						uni.hideLoading();
					});
				}

			},
		},
		onReachBottom: function() {
			if (this.page < this.pages) {
				this.page += 1;
				this.getMoney();
			}

		},
		onPullDownRefresh: function() {
			this.page = 1;
			this.getMoney();
		}

	}
</script>

<style lang='scss'>
	@import "../../static/css/index.css";


	.item {
		background: #FFFFFF;
		padding: 32rpx;
		margin: 32rpx;
		font-size: 28rpx;
		/* box-shadow: 7px 9px 34px rgba(0, 0, 0, 0.1); */
		border-radius: 16upx;
	}
</style>