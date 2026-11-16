<template>
	<view class="container">
		<view class="list-cell b-b" @click="navTo1('/pages/member/xieyi')" hover-class="cell-hover"
		 :hover-stay-time="50">
			<text class="cell-tit">用户协议</text>
		</view>
		<view class="list-cell b-b" @click="navTo1('/pages/member/mimi')" hover-class="cell-hover"
		 :hover-stay-time="50">
			<text class="cell-tit">隐私政策</text>
		</view>
		<view class="list-cell b-b" @click="navTo1('/pages/feedbackIndex/feedbackIndex')" hover-class="cell-hover"
		 :hover-stay-time="50">
			<text class="cell-tit">帮助中心</text>
		</view>
		<view class="list-cell b-b" @click="navTo1('/pages/my/zhuxiao')" hover-class="cell-hover"
		 :hover-stay-time="50">
			<text class="cell-tit">注销账号</text>
		</view>
		<button class="confirm-btn" v-if="isShow" @click="toLogout">退出登录</button>
	</view>
</template>

<script>
	import {
		mapMutations
	} from 'vuex';
	export default {
		data() {
			return {
				isShow: false,
				isWXShow: false,
				relation_id: '',
				guanyu:''
			};
		},
		onShow() {
			let token = this.$queue.getData('token');
			if (token) {
				this.isShow = true;
			}
		},
		onLoad() {
			this.guanyu = this.$queue.getData('GYWN');
			this.isWXShow = true;
		},
		methods: {
			...mapMutations(['logout']),

			navTo(url) {
				let token = this.$queue.getData('token');
				if (token) {
					uni.navigateTo({
						url: url
					});
				} else {
					uni.navigateTo({
						url: '/pages/public/login'
					});
				}
			},
			navTo1(url) {
				uni.navigateTo({
					url: url
				});
			},
			about(url) {
				uni.showModal({
					showCancel: false,
					title: '关于我们',
					content: this.guanyu,
					success: e => {
						if (e.confirm) {}
					}
				});
			},
			//退出登录
			toLogout() {
				let that = this;
				uni.showModal({
					title: '退出提醒',
					content: '确定要退出登录么',
					success: e => {
						if (e.confirm) {
							that.$queue.remove('token');
							that.$queue.remove('userId');
							that.$queue.remove('mobile');
							that.$queue.remove('href');
							that.$queue.remove('grade');
							that.$queue.remove('nickName');
							that.$queue.remove('isInvitation');
							that.$queue.remove('image_url');
							that.$queue.remove('relation_id');
							that.$queue.remove('relation');
							that.isShow = false;
							setTimeout(() => {
								uni.navigateBack()
							}, 200);
						}
					}
				});
			}
		}
	};
</script>

<style lang="scss">
	page {
		background: #FFFFFF;
	}

	.confirm-btn {
		width: 300px;
		height: 42px;
		line-height: 42px;
		border-radius: 30px;
		margin-top: 60upx;
		background: #5CC477;
		color: #FFFFFF;
		font-size: 26rpx;

		&:after {
			border-radius: 60px;
		}
	}

	.list-cell {
		display: flex;
		align-items: baseline;
		padding: 10px;
		line-height: 30px;
		position: relative;
		background: #FFFFFF;
		justify-content: center;

		&.log-out-btn {
			margin-top: 20px;

			.cell-tit {
				
				text-align: center;
				margin-right: 0;
			}
		}

		&.cell-hover {
			
			// background: #fafafa;
		}

		&.b-b:after {
			left: 16px;
			
		}

		&.m-t {
			margin-top: 9px;
			
		}

		.cell-more {
			align-self: baseline;
			font-size: 24rpx;
			margin-left: 6px;
			
		}

		.cell-tit {
			flex: 1;
			font-size: 28rpx;
			margin-right: 6px;
			
		}

		.cell-tip {
			font-size: 24rpx;
			
		}

		switch {
			transform: translateX(8px) scale(0.84);
		}
	}
</style>
