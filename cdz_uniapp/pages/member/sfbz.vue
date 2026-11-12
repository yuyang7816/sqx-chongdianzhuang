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
		onLoad(option) {
			if(option.content){
				this.content = option.content;
			}
			
		},
        methods: {
			
		}
    }
</script>

<style>
	page{
		background: #ffffff;
	}
</style>
