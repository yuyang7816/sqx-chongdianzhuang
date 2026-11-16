<template>
	<view>

		<view class="helpTitle">{{ resultData.helpWordTitle }}</view>
		<view class="helpCons">
			<u-parse class="helpCon" :content="content"></u-parse>
		</view>


	</view>
</template>

<script>
	export default {
		data() {
			return {
				helpWordId: '',
				resultData: [],
				content: ''
			};
		},
		onLoad(options) {
			if (options.helpWordId) {
				this.helpWordId = options.helpWordId
				this.getContent()
			}
		},
		methods: {
			getContent() {
				let data = {
					helpWordId: this.helpWordId,
				}
				this.$Request.get("/app/help/selectHelpWordDetails", data).then(res => {
					if (res.code == 0) {
						uni.setNavigationBarTitle({
							title: res.data.helpWordTitle
						});
						this.resultData = res.data
						this.content = res.data.helpWordContent.replace(new RegExp("img", "g"),
							'img style="width:100%;height:auto;"')
					} else {
						uni.showToast({
							title: res.msg,
							icon: 'none'
						})
					}
				});

			}
		}
	};
</script>

<style>
	page{
		background: #FFFFFf;
	}
	.helpTitle {
		font-size: 40rpx;
		font-weight: bold;
		margin: 50rpx 30rpx 30rpx;
	}

	.helpCons {
		width: 686rpx;
		margin: 0 auto;
	}

	.helpCon {
		font-size: 30rpx;
		line-height: 2em;
	}
</style>