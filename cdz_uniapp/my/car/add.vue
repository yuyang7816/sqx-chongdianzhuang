<template>
	<view>
		<!-- 表单 -->
		<view class="from">
			<view class="from-box">
				<view class="from-box-con">
					<view class="from-box-con-item">
						<view class="tite">车辆型号</view>
						<view class="from-box-con-item-r" @tap="showCardType = true">
							<!-- <u-input placeholder="请选择车辆型号" v-model="carType" style="margin-right: 25rpx;" fontSize="12"
								inputAlign="right" :clearable="false">
							</u-input> -->
							<view v-if="carType">{{carType}}</view>
							<view v-else style="color: grey;">请选择车辆型号</view>
							<u-icon name="arrow-right" color="#CCCCCC" size="28"></u-icon>
						</view>
					</view>
					
					<view class="from-box-con-item">
						<view class="tite">车牌号码</view>
						<view class="from-box-con-item-r">
							<!-- <u-input placeholder="请输入车牌号码" v-model="carNo"  style="margin-right: 25rpx;" fontSize="12"
								inputAlign="right" :clearable="false">
							</u-input> -->
							<view style="margin-right: 25rpx;" v-if="carNo" @tap="addLicenseDialog">
								{{carNo ? carNo : '请输入车牌号码'}}
							</view>
							<view v-else style="margin-right: 25rpx;color: grey;" @tap="addLicenseDialog">请输入车牌号码
							</view>
							<cowainaddlicensee v-if="showLicenseDialog" @onCancel='cancelLicenseDialog'
								@onOk='okLicense' />
						</view>
					</view>
					<view class="from-box-con-item">
						<view class="tite">选择颜色</view>
						<view class="from-box-con-item-r">
							<u--input placeholder="请输入颜色" v-model="carColor" style="margin-right: 25rpx;" fontSize="16"
								inputAlign="right" :clearable="false" border="none" placeholderStyle="color:grey;">
							</u--input>
							
						</view>
					</view>
					<view class="from-box-con-item">
						<view class="tite">车主姓名</view>
						<view class="from-box-con-item-r">
							<u--input placeholder="请输入车主姓名" v-model="carName" style="margin-right: 25rpx;" fontSize="16"
								inputAlign="right" :clearable="false" border="none" placeholderStyle="color:grey;">
							</u--input>
						</view>
					</view>
					<view class="from-box-con-item" style="border: none;">
						<view class="tite">车主电话</view>
						<view class="from-box-con-item-r">
							<u--input placeholder="请输入车主电话" v-model="carPhone" type="number" maxlength="11" placeholderStyle="color:grey;"
								style="margin-right: 25rpx;" border="none" fontSize="16" inputAlign="right" :clearable="false">
							</u--input>
						</view>
					</view>

				</view>
			</view>
		</view>
		<!-- <u-select v-model="showCardType" :list="cardTypeSel" @confirm="cardTypeConfirm"></u-select> -->
		<u-picker :show="showCardType" :columns="cardTypeSel" keyName="label" @confirm="cardTypeConfirm" @cancel="showCardType = false"></u-picker>
		<!-- 添加车辆 -->
		<view class="addCar" v-if="!showLicenseDialog">
			<view class="addCar-box" @click="submit()" v-if="!carId">添加</view>
			<view class="addCar-box" @click="submits()" v-else>修改</view>
		</view>
	</view>
</template>

<script>
	import cowainaddlicensee from '../components/cowain-add-license/components/cowain-add-license/cowain-add-license'
	export default {
		components: {
			cowainaddlicensee
		},
		data() {
			return {
				showCardType: false,
				cardTypeSel: [],
				showLicenseDialog: false,
				carType: '',
				
				
				carNo: '',
				carColor: '',
				carName: '',
				carPhone: '',
				userId: '',
				
				carId: ''
			};
		},
		onLoad(option) {
			if (option.carId) {
				this.carId = option.carId
				this.getcarDetal()
			} else {
				this.getUserInfo();
			}
			this.userId = this.$queue.getData("userId");
			let cardTypeSel = this.$queue.getData('cardTypeSel');
			if (cardTypeSel) {
				let arr = []
				cardTypeSel = cardTypeSel.split(',');
				for (var i = 0; i < cardTypeSel.length; i++) {
					let data = {
						label: cardTypeSel[i],
						value: i
					}
					arr.push(data)
				}
				this.cardTypeSel.push(arr)
				console.log(JSON.stringify(this.cardTypeSel),this.cardTypeSel)
			}
		},
		onShow() {
		
		},
		methods: {
			cardTypeConfirm(e){
				console.log(e)
				this.carType = e.value[0].label;
				this.showCardType = false
			},
			getUserInfo() {
				this.$Request.get("/app/user/selectUserById").then(res => {
					if (res.code == 0) {
						this.carPhone = res.data.phone;
					}
				});
			},
			//显示添加车牌
			addLicenseDialog() {
				this.showLicenseDialog = true;
			},
			//取消添加车牌
			cancelLicenseDialog() {
				this.showLicenseDialog = false;
			},
			//添加车牌成功
			okLicense(license) {
				this.carNo = license
				console.log(license);
			},
			
			submit() {
				if (!this.carType) {
					uni.showToast({
						title: '请选择车辆型号',
						icon: 'none'
					})
					return
				}
				
				if (!this.carNo) {
					uni.showToast({
						title: '请填写车牌号码',
						icon: 'none'
					})
					return
				}
				if (!this.carColor) {
					uni.showToast({
						title: '请填写车辆颜色',
						icon: 'none'
					})
					return
				}
				if (!this.carName) {
					uni.showToast({
						title: '请填写车主姓名',
						icon: 'none'
					})
					return
				}
				if (this.carPhone.length != 11) {
					uni.showToast({
						title: '请填写正确车主电话',
						icon: 'none'
					})
					return
				}
				let data = {
					carType: this.carType,
					
					carNo: this.carNo,
					carColor: this.carColor,
					carName: this.carName,
					carPhone: this.carPhone,
					userId: this.userId,
					
				}

				this.$Request.postJson("/app/car/insertCar", data).then(res => {
					if (res.code == 0) {
						uni.removeStorageSync('carClass');
						uni.showToast({
							title: '添加成功！',
							icon: 'none'
						})
						uni.navigateBack()
					}
				})
			},
			submits() {
				if (!this.carType) {
					uni.showToast({
						title: '请选择车辆型号',
						icon: 'none'
					})
					return
				}
				

				if (!this.carNo) {
					uni.showToast({
						title: '请填写车牌号码',
						icon: 'none'
					})
					return
				}
				if (!this.carColor) {
					uni.showToast({
						title: '请填写车辆颜色',
						icon: 'none'
					})
					return
				}
				if (!this.carName) {
					uni.showToast({
						title: '请填写车主姓名',
						icon: 'none'
					})
					return
				}
				if (this.carPhone.length != 11) {
					uni.showToast({
						title: '请填写正确车主电话',
						icon: 'none'
					})
					return
				}
				let data = {
					carType: this.carType,
					
					carNo: this.carNo,
					carColor: this.carColor,
					carName: this.carName,
					carPhone: this.carPhone,
					userId: this.userId,
					carId: this.carId
				}

				this.$Request.postJson("/app/car/updateCar", data).then(res => {
					if (res.code == 0) {
						uni.removeStorageSync('carClass');
						uni.showToast({
							title: '添加成功！',
							icon: 'none'
						})
						uni.navigateBack()
					}
				})
			},
			getcarDetal() {
				let data = {
					carId: this.carId
				}
				this.$Request.get("/app/car/selectCarDetails", data).then(res => {
					if (res.code == 0) {
						this.carType = res.data.carType
						
						this.carNo = res.data.carNo
						this.carColor = res.data.carColor
						this.carName = res.data.carName
						this.carPhone = res.data.carPhone
						
					}
				})
			}
		}
	}
</script>

<style lang="less">
	page {
		background-color: #F5F5F5;
	}

	.from {
		width: 100%;
		height: 540rpx;
		display: flex;
		justify-content: center;
		margin-top: 20rpx;

		.from-box {
			width: 686rpx;
			height: 100%;
			border-radius: 24rpx;
			background-color: #ffffff;
			display: flex;
			justify-content: center;

			.from-box-con {
				width: 612rpx;
				height: 100%;
			}

			.from-box-con-item {
				width: 100%;
				height: 106rpx;
				border-bottom: 1rpx solid #F2F2F2;
				display: flex;
				justify-content: space-between;
				align-items: center;



				.from-box-con-item-r {
					display: flex;
					align-items: center;
					font-size: 28upx;
				}
			}
		}
	}

	.tite {
		font-size: 28rpx;
		color: #333333;
		font-weight: bold;
		width: 200upx;
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