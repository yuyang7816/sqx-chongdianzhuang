<template>
	<view>
		<!-- 车辆列表 -->
		<view class="carList">
			<view class="carList-box">
				<view class="carList-box-item" v-for="(item,index) in dataList" :key="index" @click.stop="getcar(item)">
					<view class="flex align-center">
						<view class="carList-box-item-r">
							<view class="carList-box-item-r-name">{{item.carNo}}</view>
							<view class="carList-box-item-r-label">
							</span>{{item.carType}}<span>|</span>{{item.carColor}}
							</view>
						</view>
					</view>
					<view class="flex align-end justify-end margin-right-sm margin-top">
						<view class="flex align-center text-right">
							<view @click.stop="bianji(item.carId)">
								<image src="../static/bianji.png" style="width: 35upx;height: 35upx;"></image>
							</view>
							<view class="margin-left" @click.stop="dete(item.carId)">
								<image src="../static/dete.png" style="width: 35upx;height: 38upx;"></image>
							</view>
						</view>
					</view>
				</view>
			</view>
		</view>
		<!-- 添加车辆 -->
		<view class="addCar">
			<view class="addCar-box" @click="gotoAddCar()">
				添加车辆
			</view>
		</view>
		<empty v-if="dataList.length == 0"></empty>
	</view>
</template>

<script>
	import empty from '../../components/empty.vue'
	export default {
		components: {
			empty
		},
		data() {
			return {
				dataList: [],
				page: 1,
				limit: 10,
				type:''
			};
		},
		onLoad(option) {
			uni.showLoading({
				title: '加载中....'
			})
			// this.getcarlist()
			if(option.type){
				this.type = option.type
			}
		},
		onShow() {
			this.getcarlist()
		},
		methods: {
			getcar(e){
				if(this.type==1){
					
				}else{
					uni.setStorageSync("carlist",e)
					uni.navigateBack()
				}
				
				
			},
			//添加车辆
			gotoAddCar() {
				uni.navigateTo({
					url: './add'
				})
			},
			//重新编辑
			bianji(carId){
				uni.navigateTo({
					url:'/my/car/add?carId='+carId
				})
			},
			//删除
			dete(carId) {
				uni.showLoading({
					title: '正在删除'
				})
				let data = {
					carId: carId
				}
				this.$Request.post("/app/car/deleteCar", data).then(res => {
					uni.hideLoading();
					if (res.code == 0) {
						uni.showToast({
							title: '删除成功',
							icon: 'none'
						})
						this.page = 1;
						this.getcarlist()
					} else {
						console.log(res.msg)
					}
				})
			},
			getcarlist() {

				let data = {
					page: this.page,
					limit: this.limit
				}
				this.$Request.get("/app/car/selectCarList", data).then(res => {
					uni.hideLoading();
					if (res.code == 0) {
						if (this.page == 1) {
							this.dataList = res.data.records
						} else {
							this.dataList = [...this.dataList, ...res.data.records]
						}
					} else {
						console.log(res.msg)
					}
					uni.stopPullDownRefresh();
				})
			}
		},
		onReachBottom: function() {
			this.page = this.page + 1;
			this.getcarlist()
		},
		onPullDownRefresh: function() {
			this.page = 1;
			this.getcarlist()
		},
	}
</script>

<style lang="scss">
	page {
		background: #F5F5F5;
	}

	.carList {
		width: 100%;
		height: auto;
		display: flex;
		justify-content: center;
		align-items: center;
		

		.carList-box {
			width: 686rpx;
			height: auto;

			.carList-box-item {
				width: 100%;
				background-color: #FFFFFF;
				border-radius: 16rpx;
				margin-top: 20rpx;
				padding: 20upx;
			}

			.carList-box-item-l {
				width: 180rpx;
				height: 75rpx;
				margin-left: 30rpx;

				image {
					width: 100%;
					height: 100%;
				}
			}

			.carList-box-item-r {
				margin-left: 17rpx;

				.carList-box-item-r-name {
					color: #1E1F31;
					font-size: 28rpx;
					font-weight: bold;
				}

				.carList-box-item-r-label {
					color: #999999;
					font-size: 24rpx;
					margin-top: 10rpx;

					span {
						margin: 0 10rpx 0 10rpx;
					}
				}
			}
		}
	}


	.addCar {
		width: 100%;
		height: 150rpx;
		display: flex;
		justify-content: center;
		position: fixed;
		bottom: 0;
		background-color: #F5F5F5;
		z-index: 999;

		.addCar-box {
			width: 686rpx;
			height: 88rpx;
			border-radius: 44rpx;
			background-color: #5CC477;
			color: #FFFFFF;
			font-size: 28rpx;
			font-weight: bold;
			display: flex;
			justify-content: center;
			align-items: center;
		}
	}
</style>