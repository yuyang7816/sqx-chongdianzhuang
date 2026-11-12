<template>
	<div class="nf-page shop-amend2-page">
		<div class="shop-amend2-page__back">
			<a href="#" @click.prevent="prev" class="shop-amend2-page__back-link">
				<icon-svg name="jiantou" class="shop-amend2-page__back-icon"></icon-svg>
				返回
			</a>
		</div>

		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">商家管理</span>
			<h2 class="nf-page-hero__title">充电桩修改</h2>
			<p class="nf-page-hero__desc">编辑充电桩基本信息，包括名称、类型、排序与销量，保存后返回台球馆详情。</p>
			<div class="nf-page-hero__bar"></div>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">
				<i class="el-icon-edit nf-filter-card__head-icon"></i>
				<span>基本信息</span>
			</div>
			<div class="shop-amend2-page__form-body">
		<el-row v-if="tearoomType==2">
			<el-col :span="18">
				<span class="shop-amend2-page__field-label">充电桩类型：</span>
				<el-radio-group v-model="goodsCategory">
					<el-radio :label="1">快充</el-radio>
					<el-radio :label="2">慢充</el-radio>
				</el-radio-group>
			</el-col>
		</el-row>
		<el-row>
			<el-col :span="18">
				<span class="shop-amend2-page__field-label">充电桩名称：</span>
				<el-input class="shop-amend2-page__field-input margin15" placeholder="请输入充电桩名称" v-model="title" autosize>
				</el-input>
			</el-col>
		</el-row>
		<!-- <el-row>
			<el-col :span="18">
				<span style=" display: inline-block;text-align: left;width: 100px;">充电桩信息：</span>
				<el-input style="width:  50%" class="margin15" placeholder="请输入充电桩信息" type="text" :controls="false"
					v-model="goodsType" autosize></el-input>
			</el-col>
		</el-row> -->
		<!-- <el-row>
			<el-col :span="18">
				<span style=" display: inline-block;text-align: left;width: 100px;">小时价：</span>
				<el-input style="width:  50%" class="margin15" placeholder="请输入小时价" type="number" :min="0"
					:controls="false" v-model="hourPrice" autosize></el-input>
			</el-col>
		</el-row>
		<el-row>
			<el-col :span="18">
				<span style=" display: inline-block;text-align: left;width: 100px;">分钟价：</span>
				<el-input style="width:  50%" class="margin15" placeholder="请输入分钟价" type="number" :min="0"
					:controls="false" v-model="minutePrice" autosize></el-input>
			</el-col>
		</el-row> -->
		<!-- <el-row>
			<el-col :span="18">
				<span style=" display: inline-block;text-align: left;width: 100px;">押金：</span>
				<el-input style="width:  50%" class="margin15" placeholder="请输入押金" type="number" :min="0"
					:controls="false" v-model="cashPledge" autosize></el-input>
			</el-col>
		</el-row> -->
		<!-- <el-row>
			<el-col :span="18">
				<span style=" display: inline-block;text-align: left;width: 100px;">预约价格：</span>
				<el-input style="width:  50%" class="margin15" placeholder="请输入预约价格" type="number" :min="0"
					:controls="false" v-model="subscription" autosize></el-input>
			</el-col>
		</el-row> -->
		<!-- <el-row>
			<el-col :span="18">
				<span style=" display: inline-block;text-align: left;width: 100px;">语音播报token：</span>
				<el-input style="width: 50%;" class="margin15" placeholder="请输入语音播报token" v-model="yyToken"
					autosize></el-input>
			</el-col>
		</el-row>
		<el-row>
			<el-col :span="18">
				<span style=" display: inline-block;text-align: left;width: 100px;">语音播报ID：</span>
				<el-input style="width: 50%;" class="margin15" placeholder="请输入语音播报ID" v-model="yyId"
					autosize></el-input>
			</el-col>
		</el-row> -->

		<el-row>
			<el-col :span="18">
				<span class="shop-amend2-page__field-label">排序：</span>
				<el-input class="shop-amend2-page__field-input margin15" placeholder="请输入排序" type="number" :min="0" v-model="sort"
					autosize></el-input>
			</el-col>
		</el-row>

		<el-row>
			<el-col :span="18">
				<span class="shop-amend2-page__field-label">充电桩销量：</span>
				<el-input class="shop-amend2-page__field-input margin15" v-model="sales" placeholder="请输入充电桩销量" type="number"
					:controls="false" autosize></el-input>
			</el-col>
		</el-row>
		
		
		<!-- <el-row>
			<el-col :span="18">
				<span style=" display: inline-block;text-align: left;width: 100px;">推流链接：</span>
				<el-input style="width: 50%" class="margin15" v-model="warm_reminder" placeholder="请输入推流链接" type="text"
					autosize></el-input>
			</el-col>
		</el-row>
		<el-row>
			<el-col :span="18">
				<span style=" display: inline-block;text-align: left;width: 100px;">摄像头配置：</span>
				<el-input style="width: 50%" class="margin15" v-model="cameraId" placeholder="请输入摄像头配置" type="text"
					autosize></el-input>
			</el-col>
		</el-row> -->
		<!-- <el-row>
			<el-col :span="18" style="margin-top: 10px;">
				<span style=" display: inline-block;text-align: left;width: 100px;">是否线下开台：</span>
				<el-radio-group v-model="isOpen">
					<el-radio :label="1">是</el-radio>
					<el-radio :label="2">否</el-radio>
				</el-radio-group>
			</el-col>
		</el-row> -->

		<!-- <el-row>
			<el-col :span="24">
				<span style=" display: inline-block;text-align: left;width: 100px;">子设备下的通道号：</span>
				<el-input style="width: 50%" class="margin15" v-model="channels" placeholder="请输入子设备下的通道号"
					type="number" :controls="false" autosize></el-input>
			</el-col>
		</el-row> -->

		<!-- <div class="content_item">
			<span  style=" display: inline-block;text-align: left;width: 100px;">充电桩标签:</span>
			<div style="display: inline-block;width: auto;">
				<span v-if="store_lable" style="display: inline-block;width: auto;">
					<el-tag v-for="(item,index) of store_lable" :key="index" type="info" closable
						@close="bqClose(index)">{{item}}</el-tag>
				</span>
				<el-input style="width:150px;" class="margin15" placeholder="请输入充电桩标签" v-model="bq" autosize
					@keydown.enter.native="addBq"></el-input>
				<el-button type="primary" icon="el-icon-edit" @click="addBq"></el-button>
			</div>
		</div>
		
		 -->
		<!-- <div class="tinymce-content" style="display: flex">
			<div style="width: 90px">充电桩详情：</div>
			<quill-editor ref="myTextEditor" v-model="content" :options="quillOptions"
				style=" padding-bottom: 50px;height: 300px;width: 72%;display: inline-table;margin-bottom: 60px;">
			</quill-editor>
		</div> -->
		<!-- <div style="display: flex; align-items: center; margin: 2% 0">
			<span style="display: inline-block; text-align: right">使用前图片：</span>
			<div style="
		  width: 148px;
		  height: 148px;
		  background-color: #fbfdff;
		  border: 1px dashed #c0ccda;
		  border-radius: 6px;
		  text-align: center;
		  line-height: 148px;
		">
				<el-upload class="avatar-uploader" v-model="goods_image" :action="$http.adornUrlUp('alioss/upload')"
					:show-file-list="false" :on-success="handleAvatarSuccess1">
					<img v-if="goods_image" :src="goods_image" class="avatar" style="width: 148px; height: 148px" />
					<i v-else class="el-icon-plus avatar-uploader-icon" style="font-size: 28px; color: #8c939d"></i>
				</el-upload>
			</div>
		</div>
		<div style="display: flex; align-items: center; margin: 2% 0">
			<span style="display: inline-block; text-align: right">使用后图片：</span>
			<div style="
		  width: 148px;
		  height: 148px;
		  background-color: #fbfdff;
		  border: 1px dashed #c0ccda;
		  border-radius: 6px;
		  text-align: center;
		  line-height: 148px;
		">
				<el-upload class="avatar-uploader" v-model="goodsTitleImage" :action="$http.adornUrlUp('alioss/upload')"
					:show-file-list="false" :on-success="handleAvatarSuccess2">
					<img v-if="goodsTitleImage" :src="goodsTitleImage" class="avatar"
						style="width: 148px; height: 148px" />
					<i v-else class="el-icon-plus avatar-uploader-icon" style="font-size: 28px; color: #8c939d"></i>
				</el-upload>
			</div>
		</div>
		<div style="display: flex; align-items: center; margin: 2% 0">
			<span style="display: inline-block; text-align: right">图标：</span>
			<div style="
		  width: 148px;
		  height: 148px;
		  background-color: #fbfdff;
		  border: 1px dashed #c0ccda;
		  border-radius: 6px;
		  text-align: center;
		  line-height: 148px;
		">
				<el-upload class="avatar-uploader" v-model="iconImg" :action="$http.adornUrlUp('alioss/upload')"
					:show-file-list="false" :on-success="handleAvatarSuccess3">
					<img v-if="iconImg" :src="iconImg" class="avatar" style="width: 148px; height: 148px" />
					<i v-else class="el-icon-plus avatar-uploader-icon" style="font-size: 28px; color: #8c939d"></i>
				</el-upload>
			</div>
		</div> -->

			</div>
		</div>

		<div class="nf-filter-card shop-amend2-page__actions">
			<el-button type="primary" icon="el-icon-back" @click="prev">返回上一页</el-button>
			<el-button type="primary" icon="el-icon-check" v-loading="tableDataLoading" @click="artiReleass">保存</el-button>
		</div>
	</div>
</template>

<script>
	import {
		quillEditor
	} from 'vue-quill-editor'
	import 'quill/dist/quill.core.css'
	import 'quill/dist/quill.snow.css'
	import 'quill/dist/quill.bubble.css'
	import quillConfig from '../selfShop/quill-config.js'
	import {
		indexOf
	} from 'lodash'

	export default {
		name: 'Tinymce',
		components: {
			quillEditor,
		},
		data() {
			return {
				content: '',
				type: '',
				title: '',
				price: '',
				goods_image: '',
				goodsTitleImage: '',
				iconImg: '',
				img: '',
				status: '',
				attr: [],
				height: 435,
				// 富文本内容
				quillOptions: quillConfig,

				//台球馆套餐id值
				tcitem: [],
				package_price: '',
				statusmain: [],
				store_lable: [],
				bq: '',
				goods_attr: '',
				goods_price: '',
				door_pwd: '',
				room_num: '',
				user_time: '',
				warm_reminder: '',
				cameraId: '',
				isOpen:1,
				wifi_pwd: '',
				sales: '',
				channels: '',
				item: [],
				itemid: '',
				tableDataLoading: false,
				goodsCategory: 2,
				hourPrice: '',
				goodsType: '',
				minutePrice: '',
				cashPledge: '',
				subscription: '',
				yyToken: '',
				yyId: '',
				sort: '',
				broadcastContent: '',
				weightRange:'',
				weightNum:'',
				tearoomType:1,

			}
		},
		methods: {
			// 返回上一级
			prev() {
				let storeId = this.$route.query.storeId
				let merchantId = this.$route.query.merchantId
				// this.$router.push({
				// 	path: '/merchIncome',
				// 	query: {
				// 		storeId: storeId,
				// 		merchantId: merchantId,
				// 		activeName: 'shop'
				// 	}
				// });
				if (this.goodsCategory == 1) {
					this.$router.push({
						path: '/merchIncome',
						query: {
							storeId: storeId,
							merchantId: merchantId,
							activeName: 'shop'
						}
					});
				} else if (this.goodsCategory == 2) {
					this.$router.push({
						path: '/merchIncome',
						query: {
							storeId: storeId,
							merchantId: merchantId,
							activeName: 'shopQp'
						}
					});
				}
				 // else if (this.goodsCategory == 3) {
				// 	this.$router.push({
				// 		path: '/merchIncome',
				// 		query: {
				// 			storeId: storeId,
				// 			merchantId: merchantId,
				// 			activeName: 'shopQtp'
				// 		}
				// 	});
				// } else if (this.goodsCategory == 4) {
				// 	this.$router.push({
				// 		path: '/merchIncome',
				// 		query: {
				// 			storeId: storeId,
				// 			merchantId: merchantId,
				// 			activeName: 'shopQgz'
				// 		}
				// 	});
				// }
			},
			select(item) {
				this.item.push(item)
				var goodsid = []
				this.item.forEach(index => {
					goodsid.push(index.id)
				})
				var itemidd = this.itemid
				// console.log(itemidd)
				var d = goodsid.toLocaleString();
				var result = d.concat(",", itemidd);
				this.goods_cate_id = result
				console.log(this.goods_cate_id)
			},
			// 添加标签
			addBq() {
				if (this.bq == '' || this.bq == ' ') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入标签名',
						type: 'warning'
					});
					return
				} else {
					console.log('this.bq', this.bq)
					this.store_lable.push(this.bq)
					this.bq = ''
				}

			},
			// 删除标签
			bqClose(index) {
				this.store_lable.splice(index, 1);
			},
			handleAvatarSuccess1(file, fileList) {
				this.goods_image = file.data
			},
			handleAvatarSuccess2(file, fileList) {
				this.goodsTitleImage = file.data;
			},
			handleAvatarSuccess3(file, fileList) {
				this.iconImg = file.data;
			},

			init() {
				let id = this.$route.query.id
				let storeId = this.$route.query.storeId
				let merchantId = this.$route.query.merchantId
				this.$http({
					url: this.$http.adornUrl('goods/selectGoodsById'),
					method: 'get',
					params: this.$http.adornParams({
						'goodsId': id,
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.id = data.data.goodsId;
						this.title = data.data.goodsName
						this.goods_attr = data.data.goodsType
						this.door_pwd = data.data.roomPwd
						this.goods_price = data.data.price
						this.room_num = data.data.roomNo
						this.user_time = data.data.endTime
						this.warm_reminder = data.data.goodsTips
						this.cameraId = data.data.cameraId
						this.wifi_pwd = data.data.roomWifi
						this.content = data.data.goodsDetails
						if (data.data.productIds) {
							var bixuanid = data.data.productIds
							var d = bixuanid
							this.itemid = bixuanid
							var index1 = d.split(",");
							var number = index1.map(Number)
							this.tcitem = number
						}
						if(data.data.isOpen){
							this.isOpen = data.data.isOpen
						}else{
							this.isOpen = 1
						}
						// this.package_price = data.data.package_price
						this.sales = data.data.sales
						this.channels = data.data.channels
						this.goods_image = data.data.goodsCoverImage
						this.goodsTitleImage = data.data.goodsTitleImage
						this.iconImg = data.data.iconImg
						// if(data.data.goodsCategory){
						// 	this.goodsCategory = Number(data.data.goodsCategory) 
						// }else{
						// 	this.goodsCategory = 1
						// }
						this.weightRange = data.data.weightRange
						this.weightNum = data.data.weightNum
						this.goodsType = data.data.goodsType
						this.hourPrice = data.data.hourPrice
						this.minutePrice = data.data.minutePrice
						this.cashPledge = data.data.cashPledge
						this.subscription = data.data.subscription
						this.yyToken = data.data.yyToken
						this.yyId = data.data.yyId
						this.sort = data.data.sort
						if (data.data.labels && data.data.labels != '') {
							let goodsLabel = data.data.labels.split(',')
							this.store_lable = goodsLabel
						}
					}
				})
			},
			//充电桩套餐
			getshoptc() {
				let storeId = this.$route.query.storeId
				this.$http({
					url: this.$http.adornUrl('product/selectProductList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': '',
						'limit': '',
						'productName': '',
						'tearoomId': storeId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.statusmain = returnData
				})
			},
			artiReleass() {
				let storeId = this.$route.query.storeId
				let merchantId = this.$route.query.merchantId


				// if (this.goodsCategory == 1) {
					if (this.title == "") {
						this.$notify({
							title: "提示",
							duration: 1800,
							message: "请输入充电桩标题",
							type: "warning",
						});
						return;
					}
				// }
				// if (this.goodsCategory == 2) {
				// 	if (this.title == "") {
				// 		this.$notify({
				// 			title: "提示",
				// 			duration: 1800,
				// 			message: "请输入棋牌桌标题",
				// 			type: "warning",
				// 		});
				// 		return;
				// 	}
				// }
				// if (this.goodsCategory == 3) {
				// 	if (this.title == "") {
				// 		this.$notify({
				// 			title: "提示",
				// 			duration: 1800,
				// 			message: "请输入其他桌标题",
				// 			type: "warning",
				// 		});
				// 		return;
				// 	}
				// }
				// if (this.goodsCategory == 4) {
				// 	if (this.title == "") {
				// 		this.$notify({
				// 			title: "提示",
				// 			duration: 1800,
				// 			message: "请输入柜子标题",
				// 			type: "warning",
				// 		});
				// 		return;
				// 	}
					
				// 	if (this.weightNum == ""||this.weightNum==null) {
				// 		this.$notify({
				// 			title: "提示",
				// 			duration: 1800,
				// 			message: "请输入重量",
				// 			type: "warning",
				// 		});
				// 		return;
				// 	}
				// 	if (this.weightRange == ""||this.weightRange==null) {
				// 		this.$notify({
				// 			title: "提示",
				// 			duration: 1800,
				// 			message: "请输入误差范围",
				// 			type: "warning",
				// 		});
				// 		return;
				// 	}
					
				// }
				

				// if (this.hourPrice === "") {
				// 	this.$notify({
				// 		title: "提示",
				// 		duration: 1800,
				// 		message: "请输入小时价",
				// 		type: "warning",
				// 	});
				// 	return;
				// }
				// if (this.minutePrice === "") {
				// 	this.$notify({
				// 		title: "提示",
				// 		duration: 1800,
				// 		message: "请输入分钟价",
				// 		type: "warning",
				// 	});
				// 	return;
				// }
				// if (this.cashPledge === "") {
				// 	this.$notify({
				// 		title: "提示",
				// 		duration: 1800,
				// 		message: "请输入押金",
				// 		type: "warning",
				// 	});
				// 	return;
				// }
				// if (this.subscription === "") {
				// 	this.$notify({
				// 		title: "提示",
				// 		duration: 1800,
				// 		message: "请输入预约价格",
				// 		type: "warning",
				// 	});
				// 	return;
				// }
				// if (this.channels === "" || this.channels == null) {
				// 	this.$notify({
				// 		title: "提示",
				// 		duration: 1800,
				// 		message: "请输入子设备下的通道号",
				// 		type: "warning",
				// 	});
				// 	return;
				// }

				// if (this.warm_reminder == '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请输入提示信息',
				// 		type: 'warning'
				// 	});
				// 	return
				// }
				// if (this.goods_image == "") {
				// 	this.$notify({
				// 		title: "提示",
				// 		duration: 1800,
				// 		message: "请上传开台前图片",
				// 		type: "warning",
				// 	});
				// 	return;
				// }
				// if (this.goodsTitleImage == "") {
				// 	this.$notify({
				// 		title: "提示",
				// 		duration: 1800,
				// 		message: "请上传开台后图片",
				// 		type: "warning",
				// 	});
				// 	return;
				// }
				// if (this.iconImg == "") {
				// 	this.$notify({
				// 		title: "提示",
				// 		duration: 1800,
				// 		message: "请上传桌台图标",
				// 		type: "warning",
				// 	});
				// 	return;
				// }


				// if (this.goodsType == "") {
				// 	this.$notify({
				// 		title: "提示",
				// 		duration: 1800,
				// 		message: "请输入桌台信息",
				// 		type: "warning",
				// 	});
				// 	return;
				// }

				let goodsLabel = this.store_lable.toString()
				// if (this.goodsLabel == '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请输入标签',
				// 		type: 'warning'
				// 	});
				// 	return
				// }
				this.tableDataLoading = true
				let id = this.$route.query.id
				this.$http({
					url: this.$http.adornUrl("goods/updateGoods "),
					method: "post",
					data: this.$http.adornData({
						'goodsId': id,
						'tearoomId': storeId,
						'goodsName': this.title,
						'goodsType': this.goods_attr,
						'price': this.goods_price,
						'roomPwd': this.door_pwd,
						'goodsCoverImage': this.goods_image,
						'goodsTitleImage': this.goodsTitleImage,
						'iconImg': this.iconImg,
						'goodsDetails': this.content,
						'roomNo': this.room_num,
						'endTime': this.user_time,
						'goodsTips': this.warm_reminder,
						'roomWifi': this.wifi_pwd,
						'productIds': this.tcitem.toString(), //充电桩套餐id
						'labels': goodsLabel, //标签
						'sales': this.sales,
						'channels': this.channels,
						'goodsCategory': this.goodsCategory,
						'hourPrice': this.hourPrice,
						'minutePrice': this.minutePrice,
						'cashPledge': this.cashPledge,
						'subscription': this.subscription,
						'yyToken': this.yyToken,
						'yyId': this.yyId,
						'goodsType': this.goodsType,
						'sort': this.sort,
						'cameraId': this.cameraId,
						'isOpen':this.isOpen,
						'weightRange':this.weightRange,
						'weightNum':this.weightNum,

					}),
				}).then(({
					data
				}) => {
					if (data && data.code == 0) {
						this.$message({
							message: "操作成功",
							type: "success",
							duration: 1500,
							onClose: () => {
								this.tableDataLoading = false
								this.title = "";
								this.type = "";
								this.merchants = "";
								this.goods_price = "";
								this.door_pwd = "";
								this.goods_attr = "";
								this.content = "";
								this.postagePrice = "";
								this.store_type_id = "";
								if (this.goodsCategory == 1) {
									this.$router.push({
										path: '/merchIncome',
										query: {
											storeId: storeId,
											merchantId: merchantId,
											activeName: 'shop'
										}
									});
								} else if (this.goodsCategory == 2) {
									this.$router.push({
										path: '/merchIncome',
										query: {
											storeId: storeId,
											merchantId: merchantId,
											activeName: 'shopQp'
										}
									});
								}
								 // else if (this.goodsCategory == 3) {
								// 	this.$router.push({
								// 		path: '/merchIncome',
								// 		query: {
								// 			storeId: storeId,
								// 			merchantId: merchantId,
								// 			activeName: 'shopQtp'
								// 		}
								// 	});
								// }else if (this.goodsCategory == 4) {
								// 	this.$router.push({
								// 		path: '/merchIncome',
								// 		query: {
								// 			storeId: storeId,
								// 			merchantId: merchantId,
								// 			activeName: 'shopQgz'
								// 		}
								// 	});
								// }
							},
						});
					} else {
						this.$message({
							message: data.msg,
							type: 'warning',
							duration: 1500,
							onClose: () => {
								this.$router.push({
									path: '/merchIncome',
									query: {
										storeId: storeId,
										merchantId: merchantId,
										activeName: 'shop'
									}
								});
							}
						})
					}

				});
			},
			dataSelect1(){
				this.goodsCategory =  this.$route.query.goodsCategory
				this.tearoomType = this.$route.query.tearoomType
				this.init()
				// this.getshoptc()
			},
		},
		mounted() {
			this.goodsCategory = this.$route.query.goodsCategory
			this.tearoomType = this.$route.query.tearoomType
			this.init()
			// this.getshoptc()
		},
		watch: {
		  '$route': 'dataSelect1'
		}
	}
</script>

<style scoped>
.shop-amend2-page {
	overflow-x: hidden;
	padding-bottom: 16px;
}

.shop-amend2-page__back {
	margin-bottom: 12px;
}

.shop-amend2-page__back-link {
	display: inline-flex;
	align-items: center;
	font-size: 14px;
	color: #606266;
	text-decoration: none;
}

.shop-amend2-page__back-link:hover {
	color: #5cc477;
}

.shop-amend2-page__back-icon {
	width: 1.2em;
	height: 1.2em;
	margin-right: 4px;
}

.shop-amend2-page__form-body .el-col {
	margin-bottom: 4px;
	font-size: 14px;
	color: #606266;
}

.shop-amend2-page__field-label {
	display: inline-block;
	text-align: left;
	width: 100px;
	font-size: 14px;
	color: #606266;
}

.shop-amend2-page__field-input {
	width: 50%;
}

.shop-amend2-page__actions {
	display: flex;
	justify-content: center;
	align-items: center;
	gap: 16px;
	margin-bottom: 0;
}

.shop-amend2-page__actions .el-button + .el-button {
	margin-left: 0;
}

.shop-amend2-page >>> .el-radio__input.is-checked .el-radio__inner {
	border-color: #5cc477;
	background: #5cc477;
}

.shop-amend2-page >>> .el-radio__input.is-checked + .el-radio__label {
	color: #5cc477;
}

.hide .el-upload--picture-card {
	display: none;
}

.margin15 {
	margin-right: 15px;
	margin-top: 10px;
	display: inline-block;
}

.divhove {
	position: absolute;
	width: 100%;
	height: 100%;
	left: 0;
	top: 0;
	opacity: 0;
	cursor: default;
	text-align: center;
	padding-top: 50%;
	border-radius: 6px;
	background-color: rgba(0, 0, 0, .5);
	transition: opacity .3s;
}

.divhove:hover {
	opacity: 0.8;
}

.ruleitem {
	padding-left: 10px;
	margin-top: 15px;
}

.ruleitem .ruleicon {
	position: relative;
	top: 3px;
	left: 2px;
	width: 1.2em;
	height: 1.2em;
}

.el-tag + .el-tag {
	margin-left: 10px;
}

.el-tag--medium {
	margin-right: 10px;
}

.button-new-tag {
	height: 32px;
	line-height: 31px;
	padding-top: 0;
	padding-bottom: 0;
	border-radius: 4px;
	border-top-left-radius: 0;
	border-bottom-left-radius: 0;
	background-color: #f5f7fa;
	position: relative;
	left: -5px;
	border: 1px solid #dcdfe6;
}

.input-new-tag {
	width: 90px;
	margin-left: 10px;
	vertical-align: bottom;
}

.el-input--small .el-input__inner {
	border-top-right-radius: 0;
	border-bottom-right-radius: 0;
}

.el-table--enable-row-transition .el-table__body td {
	text-align: center;
}

.imgWrap .avatar-uploader .el-upload {
	width: 60px;
}

.el-table .cell {
	text-align: center !important;
}
</style>