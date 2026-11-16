<template>
	<view style="font-size: 14px;line-height: 26px;padding: 32upx;" class="home1 container">
		<!-- <view style="color: #ffffff;font-size: 28upx;" v-html="content"></view> -->
		<u-parse :content="content"></u-parse>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				content: ''
			}
		},
		onLoad() {
			this.getGuize();
		},
		methods: {
			getGuize() {
				this.$Request.getT('/app/common/type/187').then(res => {
					if (res.code === 0) {
						this.content = res.data.value;
					}
				});
			}
		}
	}
</script>

<style>
	page {
		background-color: #FFFFFF;
	}
</style>