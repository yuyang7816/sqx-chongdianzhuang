<template>
	<view>
		<view class="times flex align-center justify-center">
			<view class="times-time">
				预计等待：{{hours}}:{{minutes}}:{{seconds}}
			</view>
			<!-- <view class="times-item">
				{{hours>=10?hours:'0'+hours}}
			</view>
			<view class="times-dian">
				:
			</view>
			<view class="times-item">
				{{minutes>=10?minutes:'0'+minutes}}
			</view>
			<view class="times-dian">
				:
			</view>
			<view class="times-item">
				{{seconds>=10?seconds:'0'+seconds}}
			</view> -->
		</view>
	</view>
</template>

<script>
	export default {
		name: "indexListComp",
		props: {
			ordersEndTime: {
				type: String,
				required: true,
				default: '',
			}
		},
		data() {
			return {
				totalSeconds: 0,
				intervalId: null,
				hours: 0,
				minutes: 0,
				seconds: 0,
			};
		},
		mounted() {
			clearInterval(this.intervalId)
			this.intervalId = null

			if (!this.ordersEndTime) { //如果没有结束时间直接return
				return
			}
			// 每隔 1 秒更新一次倒计时
			this.countDown(this.ordersEndTime);
			this.intervalId = setInterval(() => {
				this.countDown(this.ordersEndTime);
			}, 1000);
		},
		beforeDestroy() {
			console.log('走销毁了')
			clearInterval(this.intervalId)
			this.intervalId = null
		},
		methods: {
			countDown(endTime) {
				// 获取当前时间
				var now = new Date();
				// 将结束时间字符串转换为日期对象
				var end = new Date(endTime);
				// 计算时间差（单位：毫秒）
				var diff = end - now;

				if (diff <= 0) {
					this.$emit('timeEnd')
					clearInterval(this.intervalId)
					this.intervalId = null
					this.hours = '00'
					this.minutes = '00'
					this.seconds = '00'
					return;
				}

				// 计算剩余的小时、分钟、秒
				const hours = Math.floor(diff / (1000 * 60 * 60));
				const minutes = Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60));
				const seconds = Math.floor((diff % (1000 * 60)) / 1000);

				// 添加前导零
				this.hours = hours < 10 ? `0${hours}` : hours.toString();
				this.minutes = minutes < 10 ? `0${minutes}` : minutes.toString();
				this.seconds = seconds < 10 ? `0${seconds}` : seconds.toString();
			}
		}
	}
</script>

<style lang="scss" scoped>
	.times-time {
		color: #ffffff;
		font-size: 24rpx;
	}

	.times-item {
		width: 40rpx;
		height: 40rpx;
		background-color: #086000;
		border-radius: 6rpx;
		border: 1px solid #88BC84;
		color: #ffffff;
		font-size: 24rpx;
		font-weight: 600;
		display: flex;
		align-items: center;
		justify-content: center;
	}

	.times-dian {
		color: #88BC84;
		margin: 0 6rpx;
		font-weight: 600;
	}
</style>