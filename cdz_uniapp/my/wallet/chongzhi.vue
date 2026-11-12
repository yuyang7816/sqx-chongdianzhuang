<template>
	<view>
		<view style="text-align: left;padding-bottom: 10rpx;">
			<view v-for="(item, index) in list" :key="index" class="item">
				<view>
					<view style="color: #333333;font-size: 28upx;">
						<view style="margin-bottom: 8upx">{{item.title}}</view>
						
						
						<view style="margin-bottom: 8upx">{{item.content}}</view>
						<view style="margin-bottom: 8upx"> 创建时间：{{item.createTime}}</view>
						<view style="margin-bottom: 8upx;text-align: right;">
							<text v-if="item.type == 1"
								style="color: #A375FF;font-size: 32upx;font-weight: 600"><text>+</text>{{item.money}}元</text>
							<text v-if="item.type == 2"
								style="color:red;font-size: 32upx;font-weight: 600"><text>-</text>{{item.money}}元</text>
						</view>
					</view>
				</view>
			</view>
			
			<empty v-if="list.length == 0" content="暂无明细"></empty>
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
				list: [],
				page: 1,
				limit: 10,
				totalPage: 1,
				tabIndex: 1,
				checkReZhiShu: '否',
				checkReTuanZhang: '否',
				checkReFeiZhiShu: '否',
				scrollTop: false,
				contentText: {
					contentdown: '上拉显示更多',
					contentrefresh: '正在加载...',
					contentnomore: '没有更多数据了'
				}
			}
		},
		onLoad() {
			this.$queue.showLoading("加载中...");
			this.getList();
		},
		onPageScroll: function(e) {
			this.scrollTop = e.scrollTop > 200;
		},
		methods: {
			getList() {
				let data = {
					page: this.page,
					limit: this.limit
				}
				this.$Request.getT('/app/cash/selectUserRechargeByUserId', data).then(res => {
					uni.hideLoading()
					uni.stopPullDownRefresh()
					if (res.code == 0) {
						this.totalPage = res.data.totalPage
						if (this.page == 1) {
							this.list = res.data.list
						} else {
							this.list = [...this.list, ...res.data.list]
						}

					}
				})
				
			}
		},
		onReachBottom: function() {
			if (this.page < this.totalPage) {
				this.page += this.page
				this.getList();
			}

		},
		onPullDownRefresh: function() {
			this.page = 1;
			this.getList();
		}
	}
</script>

<style lang="less">
	page {
		background: #F7F7F7;
	}

	.tui-tab-item-title {
		color: #333333;
		font-size: 30rpx;
		height: 80rpx;
		line-height: 80rpx;
		flex-wrap: nowrap;
		white-space: nowrap;
	}

	.tui-tab-item-title-active {
		border-bottom: 1px solid #5E81F9;
		color: #5E81F9;
		font-size: 32upx;
		font-weight: bold;
		border-bottom-width: 6upx;
		text-align: center;
	}

	.item {
		color: #333333;
		background: #FFFFFF;
		padding: 32rpx;
		margin: 32rpx;
		font-size: 28rpx;
		// box-shadow: 7px 9px 34px rgba(0, 0, 0, 0.1);
		border-radius: 16upx;
	}
</style>