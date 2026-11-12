<template>
	<view style="padding-bottom: 50upx;">
		<view class="padding-sm">
			<view class="bg text-white padding" style="border-radius: 24rpx;">
				<view class="text-lg">可用余额</view>
				<view class="flex margin-top-sm">
					<!-- <view class="text-xxl"></view> -->
					<view class="text-xxl" style="color: #333333;">{{inviteMoney}}元</view>
				</view>
			</view>
			<view class="bg text-white padding margin-top-sm inptxt" style="border-radius: 24rpx;">
				<view class="text-lg margin-bottom">提现金额</view>
				<input type="number" placeholder="请输入提现金额" v-model="moneyNum">
			</view>
			<view style="display: flex;flex-direction: row;flex-wrap: wrap;margin-top: 20upx;">
				<view style="display: flex;flex-direction: row;">
					<view class="zhifuSlect_btn" @tap="zhifuSelectTap(1)"
						:style="zhifuSelect == 1 ? 'border: 1px solid #32be62;' : 'border: 1px solid #ffffff;'">
						<image src="/static/images/my/zhifubao.png"></image>
						<view style="margin-left:10upx">支付宝</view>
					</view>
					<view class="zhifuSlect_btn" @tap="zhifuSelectTap(2)"
						:style="zhifuSelect ==2 ? 'border: 1px solid #32be62;' : 'border: 1px solid #ffffff;'">
						<image src="/static/images/my/weixin.png"></image>
						<view style="margin-left:10upx">微信</view>
					</view>
				</view>
			</view>
			<view class="text-center text-grey margin-top">{{placeholder}}</view>
			<view class="btn" @click="cashMoney">立即提现</view>



			<u-popup :show="show" mode="center" :closeable="true" close-icon-pos="top-right" close-icon="close-circle"
				close-icon-size="50" round="20" @close="closeWxImag">
				<view class="padding" style="width:600rpx;">
					<view class="text-center text-lg text-bold text-black">添加微信</view>
					<view class="text-center padding-top-sm padding-bottom-lg" style="color: red;">请提交微信收款二维码
					</view>
					<view style="width: 80%;margin: 0 auto;">
						<view class="margin-top-sm " @click="addImages"
							style="border: 4rpx solid #32be62;border-radius: 16rpx;overflow: hidden;">
							<image v-if="!wxImg" src="/shop/static/image/wallet/erweima.png"></image>
							<image v-else :src="wxImg"></image>
						</view>
					</view>
					<view class="text-center margin-top-sm " @click="SaveWxImg"
						style="border-radius: 10rpx;background-color: #32be62;color: #FFFFFF;height: 80rpx;line-height: 80rpx;">
						保存</view>
				</view>
			</u-popup>
		</view>


		<view class="flex justify-around margin-top text-white">
			<view @click="goNav('/shop/wallet/zhifubao')">提现账号</view>
			<view @click="goWxImg()">微信收款码</view>
			<view @click="goNav('/shop/wallet/getMoneyList')">钱包明细</view>
			<view @click="goNav('/shop/wallet/cashList')">提现记录</view>
		</view>
	</view>
</template>

<script>
	import configdata from '@/common/config.js';
	export default {
		data() {
			return {
				inviteMoney: 0, //可提现金额
				mtypeSelect: '',
				wxImg: '',
				mywxImg: '',
				WxTXFangShi: 1,
				show: false,
				zhifuSelect: 1,
				moneyNum: null,
				charge: 0, //提现手续费
				maxMoney: 0, //最高提现额度
				minMoney: 0, //最低提现额度
				placeholder: ''
			}
		},
		onLoad() {
			uni.setNavigationBarTitle({
				title: '提现'
			})

			this.$Request.getT('/app/common/type/244').then(res => {
				if (res.code == 0) {
					if (res.data && res.data.value) {
						this.WxTXFangShi = res.data.value;
					}
				}
			});

			this.getUserInfo()
		},
		onShow() {
			this.getMoney()
			this.getCharge()
			this.getMinMoney()
			this.getMaxMoney()
		},
		methods: {
			closeWxImag() {
				this.show = false;
			},
			goWxImg() {
				this.show = true;
			},
			SaveWxImg() {
				let that = this;
				this.$queue.showLoading('上传中...')
				if (this.wxImg == '') {
					uni.hideLoading();
					this.$queue.showToast('请上传微信二维码！')
					return;
				}

				let data = {
					wxImg: that.wxImg
				}
				that.$Request.postJson('/app/user/updateUser', data).then(res => {
					uni.hideLoading();
					if (res.code == 0) {
						that.show = false;
						that.$queue.showToast('绑定成功！')
						that.$Request.getT('/app/user/selectUserById').then(res => {
							if (res.code == 0) {
								that.mywxImg = res.data.wxImg ? res.data.wxImg : '';
							}
						});
					} else {
						that.$queue.showToast(res.msg)
					}
				});
			},
			// 图片上传
			addImages() {
				uni.chooseImage({
					count: 1,
					sourceType: ['album', 'camera'],
					success: res => {
						for (let i = 0; i < 1; i++) {
							this.$queue.showLoading("上传中...");
							uni.uploadFile({ // 上传接口
								// url: 'https://qipaishi.xianmaxiong.com/sqx_fast/alioss/upload',
								url: this.config("APIHOST") + '/alioss/upload', //真实的接口地址
								filePath: res.tempFilePaths[i],
								name: 'file',
								success: (uploadFileRes) => {
									this.wxImg = JSON.parse(uploadFileRes.data).data
									uni.hideLoading();
								}
							});
						}
					}
				})
			},
			config: function(name) {
				var info = null;
				if (name) {
					var name2 = name.split("."); //字符分割
					if (name2.length > 1) {
						info = configdata[name2[0]][name2[1]] || null;
					} else {
						info = configdata[name] || null;
					}
					if (info == null) {
						let web_config = cache.get("web_config");
						if (web_config) {
							if (name2.length > 1) {
								info = web_config[name2[0]][name2[1]] || null;
							} else {
								info = web_config[name] || null;
							}
						}
					}
				}
				return info;
			},
			zhifuSelectTap(index) {
				this.zhifuSelect = index;
			},
			getUserInfo() {
				this.$Request.get("/app/user/selectUserById").then(des => {
					if (des.code == 0) {
						this.mywxImg = des.data.wxImg ? des.data.wxImg : '';
						this.wxImg = des.data.wxImg ? des.data.wxImg : '';
					}
				});
			},
			// 提现手续费
			getCharge() {
				this.$Request.get("/app/common/type/152").then(res => {
					if (res.code == 0) {
						this.charge = parseFloat(res.data.value)
						this.placeholder = '手续费:' + parseFloat(this.charge * 100).toFixed(
								0) + '%' + ',最低提现:' +
							this.minMoney * 1 + ',最高提现:' + this.maxMoney * 1
					}
				});
			},
			// 最低提现额度
			getMinMoney() {
				this.$Request.get("/app/common/type/112").then(res => {
					if (res.code == 0) {
						this.minMoney = parseFloat(res.data.value)
						this.placeholder = '手续费:' + parseFloat(this.charge * 100).toFixed(
								0) + '%' + ',最低提现:' +
							this.minMoney * 1 + ',最高提现:' + this.maxMoney * 1
					}
				});
			},
			// 最高提现额度
			getMaxMoney() {
				this.$Request.get("/app/common/type/153").then(res => {
					if (res.code == 0) {
						this.maxMoney = parseFloat(res.data.value)
						this.placeholder = '手续费:' + parseFloat(this.charge * 100).toFixed(
								0) + '%' + ',最低提现:' +
							this.minMoney * 1 + ',最高提现:' + this.maxMoney * 1
					}
				});
			},
			goNav(url) {
				uni.navigateTo({
					url
				})
			},
			// 我的余额
			getMoney() {
				this.$Request.get("/app/userMoney/selectMyMoney").then(res => {
					if (res.code == 0 && res.data) {
						this.inviteMoney = res.data.shopMoney
					}
				});
			},
			// 提现
			cashMoney() {
				if (!this.moneyNum) {
					uni.showToast({
						icon: 'none',
						title: '请输入提现金额'
					})
					return
				}
				if (this.moneyNum > this.inviteMoney * 1) {
					uni.showToast({
						icon: 'none',
						title: '您的余额不足'
					})
					return
				}
				if (this.moneyNum < this.minMoney || this.moneyNum > this.maxMoney * 1) {
					uni.showToast({
						icon: 'none',
						title: '请输入正确的金额'
					})
					return
				}
				// if (this.moneyNum*1+this.charge*this.moneyNum > this.money*1) {
				// 	uni.showToast({
				// 		icon: 'none',
				// 		title: '您的手续费不足'
				// 	})
				// 	return
				// }
				let that = this
				let typeSelect = 1;
				if (that.zhifuSelect == 2) {
					// #ifdef H5
					typeSelect = 3;
					// #endif

					// #ifdef MP-WEIXIN
					typeSelect = 2;
					// #endif
				}
				if (that.zhifuSelect == 1) {
					let zhiFuBao = uni.getStorageSync('zhiFuBao')
					let zhiFuBaoName = uni.getStorageSync('zhiFuBaoName')
					if (!zhiFuBao && !zhiFuBaoName) {
						uni.navigateTo({
							url: '/shop/wallet/zhifubao'
						})
						return
					}
					that.sp = (that.moneyNum * that.charge).toFixed(2)
					that.mtypeSelect = typeSelect
					uni.showModal({
						title: '提现申请提示',
						content: '本次提现' + that.moneyNum + '元，服务费' + that.sp + '元，是否确认提现？',
						success: function(res) {
							if (res.confirm) {
								console.log('用户点击确定');
								that.$Request.get("/app/cash/shopCashMoney", {
									money: that.moneyNum,
									classify: that.mtypeSelect
								}).then(res => {
									if (res.code == 0) {
										uni.showToast({
											icon: 'none',
											title: res.msg
										})
										that.moneyNum = null
									} else {
										uni.showToast({
											icon: 'none',
											title: res.msg
										})
									}
									that.getMoney()
								});
							} else if (res.cancel) {
								console.log('用户点击取消');
							}
						}
					});

				} else if (that.zhifuSelect == 2) {
					if (that.WxTXFangShi == 2) {
						if (that.mywxImg) {

							that.sp = (that.moneyNum * that.charge).toFixed(2)
							that.mtypeSelect = typeSelect
							uni.showModal({
								title: '提现申请提示',
								content: '本次提现' + that.moneyNum + '元，服务费' + that.sp + '元，是否确认提现？',
								success: function(res) {
									if (res.confirm) {
										console.log('用户点击确定');
										that.$queue.showLoading("提现中...");
										that.$Request.get("/app/cash/shopCashMoney", {
											money: that.moneyNum,
											classify: that.mtypeSelect
										}).then(res => {
											uni.hideLoading();
											if (res.code == 0) {
												uni.showToast({
													icon: 'none',
													title: res.msg
												})
												that.moneyNum = null
											} else {
												uni.showToast({
													icon: 'none',
													title: res.msg
												})
											}
											that.getMoney()
										});
									} else if (res.cancel) {
										console.log('用户点击取消');
									}
								}
							});
						} else {
							that.show = true;
						}

					} else {
						that.$queue.showLoading("提现中...");
						that.$Request.get("/app/cash/shopCashMoney", {
							money: that.moneyNum,
							classify: typeSelect
						}).then(res => {
							uni.hideLoading();
							if (res.code == 0) {
								uni.showToast({
									icon: 'none',
									title: res.msg
								})
								that.moneyNum = null
							} else {
								uni.showToast({
									icon: 'none',
									title: res.msg
								})
							}
							that.getMoney()
						});
					}
				}
			}
		}
	}
</script>

<style lang="scss">
	.inptxt {
		border-bottom: 1px solid #cccccc;
		border-radius: 0 !important;
	}

	.zhifuSlect_btn {
		justify-content: center;
		border-radius: 15upx;
		padding: 20upx;
		margin-left: 20upx;
		display: flex;
		background-color: #FFFFFF;
		width: 320upx;
		align-items: center;
		height: 80upx;
		color: #000000;

		image {
			width: 50upx;
			height: 50upx;
			border-radius: 50upx;
		}
	}

	.btn {
		width: 100%;
		height: 100upx;
		background: #32be62;
		border-radius: 44upx;
		text-align: center;
		line-height: 100upx;
		margin-top: 40upx;
		font-size: 28upx;
		font-weight: bold;
		color: #FFF;
		border-radius: 50rpx;
	}
</style>