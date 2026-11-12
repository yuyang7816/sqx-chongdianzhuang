<template>
    <view style="line-height: 26px;padding: 32upx;" class="home1">
		<view style="font-size: 28upx;" v-html="content" ></view>
    </view>
</template>

<script>
    export default {
        data() {
            return {
				content:''
			}
        },
		onLoad() {
			this.getGuize();
		},
        methods: {
			getGuize(){
				// 充值协议	823
				this.$Request.getT('/app/common/type/823').then(res =>{
					if(res.code === 0){
						this.content = res.data.value;
					}
				});
			}
		}
    }
</script>

<style>
	page{
		background: #ffffff;
	}
</style>
