<template>
	<view>
		<view class="padding" v-if="goodsInfo.goodsId">
			<image :src="goodsInfo.iconImg" class="top_image" mode="aspectFill" @tap="saveImg(goodsInfo.iconImg)"></image>

			<view class="top_itemView flex justify-between">
				<view>
					<view class="item_name">{{goodsInfo.goodsName}}/{{goodsInfo.goodsType}}</view>
					<view class="item_money margin-top-xs" v-if="goodsInfo.yyId">价值：<text>￥{{goodsInfo.yyId}}</text> </view>
					<view class="margin-top-xs flex align-center">
						<view class="item_zj" v-if="goodsInfo.goodsStatus && goodsInfo.goodsStatus == 1">可租借</view>
						<view class="item_no_zj" v-if="goodsInfo.goodsStatus && goodsInfo.goodsStatus == 2">不可租借</view>
						<view class="item_jifei">计费：¥{{goodsInfo.hourPrice}}(每小时)</view>
					</view>
				</view>
			</view>
			
			<view class="top_itemView" v-if="tearoomInfo.startTime">
				<view class="item_time">营业时间：{{tearoomInfo.startTime}}-{{tearoomInfo.endTime}}</view>
				<view class="item_address flex align-center justify-between margin-top-xs">
					<view class="flex align-center">
						<image src="/static/images/index/address.png" mode=""></image>
						<view style="width: 500rpx;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;">
							{{tearoomInfo.tearoomProvince}}{{tearoomInfo.tearoomCity}}{{tearoomInfo.tearoomDistrict}}{{tearoomInfo.tearoomAddress}}
						</view>
					</view>
					<view class="flex align-center" @tap="openMap(tearoomInfo)">
						<view class="">导航</view>
						<u-icon name="arrow-right" color="#ffffff" size="14"></u-icon>
					</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				goodsId: '',
				tearoomId: '',
				goodsInfo: {
					goodsId: ''
				},
				tearoomInfo: {
					tearoomId: ''
				}
			}
		},
		onLoad(d) {
			if (d.goodsId) {
				this.goodsId = d.goodsId;
			}

			if (d.tearoomId) {
				this.tearoomId = d.tearoomId
			}
			this.getTearoomIdList();
			this.selectGoodsById();
		},
		methods: {
			openMap(item) {
				let latitude = item.tearoomLatitude
				let longitude = item.tearoomLongitude
				let name = item.tearoomName
				let address = item.tearoomAddress
				uni.openLocation({
					latitude: latitude - 0, //要去的纬度-地址       
					longitude: longitude - 0, //要去的经度-地址
					name: address, //地址名称
					address: name, //详细地址名称
					success: function() {
						console.log('导航成功');
					},
					fail: function(error) {
						console.log(error)
					}
				});
			},
			saveImg(image) {
				let that = this;
				let imgArr = []
				imgArr.push(image);
				//预览图片
				uni.previewImage({
					urls: imgArr,
					current: imgArr[0]
				});
			},
			getTearoomIdList() {
				this.$Request.getT('/app/tearoom/selectTearoomById?tearoomId=' + this.tearoomId).then(res => {
					if (res.code == 0) {
						this.tearoomInfo = res.data

					}
				})
			},
			//根据goodsId获取桌台信息
			selectGoodsById() {
				let data = {
					goodsId: this.goodsId
				}
				this.$Request.getT('/app/goods/selectGoodsById', data).then(res => {
					if (res.code == 0) {
						this.goodsInfo = res.data
					}
				})
			},
		}
	}
</script>

<style lang="scss">
	page {
		background: #1F2A25;
	}

	.top_image {
		width: 686rpx;
		height: 400rpx;
		border-radius: 24rpx;
	}

	.top_itemView {
		width: 686rpx;
		background: #F2F6FC;
		
		
		border-radius: 24rpx;
		margin-top: 30rpx;
		padding: 30rpx;
		border: 2rpx solid #7FFFDE;

		.item_name {
			font-family: PingFang SC;
			font-weight: bold;
			font-size: 34rpx;
			color: #FFFFFF;
		}

		.item_money {
			font-family: PingFang SC;
			font-weight: bold;
			font-size: 24rpx;
			color: #FFA53F;

			text {
				font-size: 34rpx;
				font-weight: bold;
			}
		}

		.item_jifei {
			margin-left: 20rpx;
			font-family: PingFang SC;
			font-weight: 500;
			font-size: 26rpx;
			color: #FFFFFF;
		}

		.item_zj {
			background: #5ED769;
			border-radius: 6rpx;
			padding: 4rpx 12rpx;
			font-size: 24rpx;
		}
		
		.item_no_zj {
			background: #FFA53F;
			border-radius: 6rpx;
			padding: 4rpx 12rpx;
			font-size: 24rpx;
		}
		
		.item_time{
			font-family: PingFang SC;
			font-weight: 500;
			font-size: 26rpx;
			color: #CCCCCC;
		}
		
		.item_address {
			font-family: PingFang SC;
			font-weight: 500;
			font-size: 26rpx;
			color: #CCCCCC;
		
			image {
				width: 29rpx;
				height: 30rpx;
				margin-right: 10rpx;
			}
		}
	}
</style>