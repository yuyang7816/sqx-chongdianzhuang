<template>
	<div class="nf-page shop-publish2-page">
		<div class="shop-publish2-page__back">
			<a href="#" @click.prevent="prev" class="shop-publish2-page__back-link">
				<icon-svg name="jiantou" class="shop-publish2-page__back-icon"></icon-svg>
				返回
			</a>
		</div>

		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">商家管理</span>
			<h2 class="nf-page-hero__title">添加充电桩</h2>
			<p class="nf-page-hero__desc">填写充电桩基本信息，包括名称、类型、排序与销量，确认发布后返回台球馆详情。</p>
			<div class="nf-page-hero__bar"></div>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">
				<i class="el-icon-edit nf-filter-card__head-icon"></i>
				<span>基本信息</span>
			</div>
			<div class="shop-publish2-page__form-body">
			<el-row v-if="tearoomType==2">
				<el-col :span="18">
					<span class="shop-publish2-page__field-label">充电桩类型：</span>
					<el-radio-group v-model="goodsCategory">
						<el-radio :label="1">快充</el-radio>
						<el-radio :label="2">慢充</el-radio>
					</el-radio-group>
				</el-col>
			</el-row>
			<el-row>
				 <!-- v-if="goodsCategory==1" -->
				<el-col :span="24">
					<span class="shop-publish2-page__field-label">充电桩名称：</span>
					<el-input class="shop-publish2-page__field-input margin15" placeholder="请输入充电桩名称" v-model="title" autosize>
					</el-input>
				</el-col>
				
				<!-- <el-col :span="6">
					<span style=" display: inline-block;text-align: left;width: 100px;">充电桩规格：</span>
					<el-input style="width: 50%" class="margin15" placeholder="请输入充电桩规格" v-model="goods_attr" autosize>
					</el-input>
				</el-col>
				 -->
				<!-- <el-col :span="6">
					<span style=" display: inline-block;text-align: left;width: 100px;">开门密码：</span>
					<el-input style="width: 50%" class="margin15" placeholder="请输入开门密码" :controls="false"
						v-model="door_pwd" autosize></el-input>
				</el-col> -->
			</el-row>
			<!-- <el-row>
				<el-col :span="24">
					<span style=" display: inline-block;text-align: left;width: 100px;">充电桩信息：</span>
					<el-input style="width: 50%" class="margin15" placeholder="请输入充电桩信息" type="text" :min="0"
						:controls="false" v-model="goodsType" autosize></el-input>
				</el-col>
			</el-row> -->
			<!-- <el-row>
				<el-col :span="24">
					<span style=" display: inline-block;text-align: left;width: 100px;">小时价：</span>
					<el-input style="width: 50%" class="margin15" placeholder="请输入小时价" type="number" :min="0"
						:controls="false" v-model="hourPrice" autosize></el-input>
				</el-col>
			</el-row>
			<el-row>
				<el-col :span="24">
					<span style=" display: inline-block;text-align: left;width: 100px;">分钟价：</span>
					<el-input style="width: 50%" class="margin15" placeholder="请输入分钟价" type="number" :min="0"
						:controls="false" v-model="minutePrice" autosize></el-input>
				</el-col>
			</el-row> -->
			<!-- <el-row>
				<el-col :span="24">
					<span style=" display: inline-block;text-align: left;width: 100px;">押金：</span>
					<el-input style="width: 50%" class="margin15" placeholder="请输入押金" type="number" :min="0"
						:controls="false" v-model="cashPledge" autosize></el-input>
				</el-col>
			</el-row> -->
			<!-- <el-row>
				<el-col :span="24">
					<span style=" display: inline-block;text-align: left;width: 100px;">预约价格：</span>
					<el-input style="width: 50%" class="margin15" placeholder="请输入预约价格" type="number" :min="0"
						:controls="false" v-model="subscription" autosize></el-input>
				</el-col>
			</el-row> -->
			<!-- <el-row>
				<el-col :span="24">
					<span style=" display: inline-block;text-align: left;width: 100px;">语音播报token：</span>
					<el-input style="width:50%;" class="margin15" placeholder="请输入语音播报token" v-model="yyToken"
						autosize></el-input>
				</el-col>
			</el-row>
			<el-row>
				<el-col :span="24">
					<span style=" display: inline-block;text-align: left;width: 100px;">语音播报ID：</span>
					<el-input style="width:50%;" class="margin15" placeholder="请输入语音播报ID" v-model="yyId"
						autosize></el-input>
				</el-col>
			</el-row> -->

			<el-row>
				<el-col :span="24">
					<span class="shop-publish2-page__field-label">排序：</span>
					<el-input class="shop-publish2-page__field-input margin15" placeholder="请输入排序" type="number" :min="0"
						v-model="sort" autosize></el-input>
				</el-col>
			</el-row>
			<el-row>
				<!-- <el-col :span="6">
					<span style=" display: inline-block;text-align: left;width: 100px;">房间号码：</span>
					<el-input style="width: 50%" class="margin15" placeholder="请输入房间号码" :controls="false"
						v-model="room_num" autosize></el-input>
				</el-col>
				<el-col :span="6">
					<span style=" display: inline-block;text-align: left;width: 100px;">套餐有效时间：</span>
					<el-input style="width: 50%" class="margin15" placeholder="请输入套餐有效时间(天)" type="number"
						:controls="false" v-model="user_time" autosize></el-input>
				</el-col> -->
				<el-col :span="24"> <!-- v-if="goodsCategory==1" -->
					<span class="shop-publish2-page__field-label">充电桩销量：</span>
					<el-input class="shop-publish2-page__field-input margin15" v-model="sales" placeholder="请输入充电桩销量" type="number"
						:controls="false" autosize></el-input>
				</el-col>
			
				
				<!-- <el-col :span="6">
					<span style=" display: inline-block;text-align: left;width: 100px;">WiFi密码：</span>
					<el-input style="width: 50%" class="margin15" placeholder="请输入WiFi密码" v-model="wifi_pwd"
						autosize></el-input>
				</el-col> -->
			</el-row>
			<!-- <el-row>
				<el-col :span="24">
					<span style=" display: inline-block;text-align: left;width: 100px;">推流链接：</span>
					<el-input style="width: 50%" class="margin15" v-model="warm_reminder" placeholder="请输入推流链接"
						type="text" autosize></el-input>
				</el-col>
			</el-row>
			<el-row>
				<el-col :span="24">
					<span style=" display: inline-block;text-align: left;width: 100px;">摄像头配置：</span>
					<el-input style="width: 50%" class="margin15" v-model="cameraId" placeholder="请输入摄像头配置" type="text"
						autosize></el-input>
				</el-col>
			</el-row> -->
			<!-- <el-row>
				<el-col :span="18" style="margin-top: 10px;">
					<span style=" display: inline-block;text-align: left;width: 100px;">是否线下开台：</span>
					<el-radio-group v-model="isOpen" style="">
						<el-radio :label="1">是</el-radio>
						<el-radio :label="2">否</el-radio>
					</el-radio-group>
				</el-col>
			</el-row> -->
			<!-- <el-row>
				<el-col :span="24" v-if="goodsCategory==1">
					<span style=" display: inline-block;text-align: left;width: 100px;">子设备下的通道号：</span>
					<el-input style="width: 50%" class="margin15" v-model="channels" placeholder="请输入子设备下的通道号"
						type="number" :controls="false" autosize></el-input>
				</el-col>
			</el-row> -->
			<!-- <div class="content_item" style="margin-top: 10px;">
				<span style=" display: inline-block;text-align: left;width: 100px;">充电桩套餐：</span>
				<div style="display: inline-block;width: auto;">
					<el-checkbox-group v-model="tcitem" style="position: relative;left: -6%;top: 0px;">
						<div v-for="(item,index) in statusmain" :key="index">
							<div>
								<el-checkbox :label="item.productId" style="margin-left: 20px;"
									@change="select(item)">{{item.productName}}</el-checkbox>
							</div>
						</div>
					</el-checkbox-group>
				</div>
			</div> -->
			<!-- <div class="content_item" >
				<span style="display: inline-block;text-align: left;width: 100px;">充电桩标签：</span>
				<div style="display: inline-block;width: auto;">
					<span v-if="store_lable" style="display: inline-block;width: auto;">
						<el-tag v-for="(item,index) of store_lable" :key="index" type="info" closable
							@close="bqClose(index)">{{item}}</el-tag>
					</span>
					<el-input style="width:150px;" class="margin15" placeholder="请输入充电桩标签" v-model="bq" autosize
						@keydown.enter.native="addBq"></el-input>
					<el-button type="primary" icon="el-icon-edit" @click="addBq"></el-button>
				</div>
			</div> -->
			<!-- <div style="display: flex; align-items: center; margin: 2% 0">
				<span style="display: inline-block; text-align: right">试用前图片：</span>
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
					<el-upload class="avatar-uploader" v-model="goodsTitleImage"
						:action="$http.adornUrlUp('alioss/upload')" :show-file-list="false"
						:on-success="handleAvatarSuccess2">
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

		<!-- <div class="content_item" style="margin-bottom: 20px;margin-top: 10px;">
			<span style="display: inline-block;text-align: left;width: 100px;">提示信息：</span>
			<el-input style="width: 50%;" placeholder="请输提示信息" v-model="warm_reminder" autosize
				></el-input>
		</div> -->
		<!-- <div class="tinymce-content" style="display: flex">
			<div style="width: 100px">充电桩详情：</div>
			<quill-editor ref="myTextEditor" v-model="content" :options="quillOptions"
				style=" padding-bottom: 50px;height: 300px;width: 72%;display: inline-table;margin-bottom: 60px;">
			</quill-editor>
		</div> -->
		<div class="nf-filter-card shop-publish2-page__actions">
			<el-button type="primary" icon="el-icon-back" @click="prev">返回上一页</el-button>
			<el-button type="primary" icon="el-icon-check" v-loading="tableDataLoading" @click="artiReleass">确认发布</el-button>
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

	// const tinymceCDN =
	//   "https://cdn.jsdelivr.net/npm/tinymce-all-in-one@4.9.3/tinymce.min.js";

	export default {
		name: "Tinymce",
		components: {
			quillEditor,
		},
		data() {
			return {
				sales: '',
				//充电桩套餐id值
				tcitem: [],
				statusmain: [],
				store_lable: [],
				bq: '',
				specif: "",
				title: "",
				merchants: "",
				goods_attr: "",
				goods_price: "",
				door_pwd: "",
				skuImg: "",
				postagePrice: "",
				room_num: "",
				user_time: '',
				package_price: '',
				warm_reminder: "",
				cameraId: '',
				isOpen:1,
				wifi_pwd: "",
				attr: [],
				valueData: [],
				goods_cate_id: [],
				status: "",
				photos: "",
				goods_image: "",
				goodsTitleImage: '',
				iconImg: '',
				id: "",
				height: 435,
				item: [],
				itemq: [],
				// 富文本内容
				content: "",
				quillOptions: quillConfig,
				tableDataLoading: false,
				channels: '',
				goodsCategory: 2,
				hourPrice: '',
				minutePrice: '',
				cashPledge: '',
				subscription: '',
				yyToken: '',
				yyId: '',
				broadcastContent: '',
				goodsType: '',
				sort: '',
				weightRange:'',
				weightNum:'',
				tearoomType:1,


			};
		},
		methods: {
			// 图标上传
			handleAvatarSuccess(file) {
				if (this.curRowIndex == 0) {
					this.skuImg = file.data;
					this.valueData[0].skuImg = this.skuImg;
					return;
				} else {
					this.skuImg = file.data;
					this.valueData[this.curRowIndex].skuImg = this.skuImg;
				}
			},
			handleAvatarSuccess1(file, fileList) {
				this.goods_image = file.data;
			},
			handleAvatarSuccess2(file, fileList) {
				this.goodsTitleImage = file.data;
			},
			handleAvatarSuccess3(file, fileList) {
				this.iconImg = file.data;
			},

			//上传成功
			handleUploadSuccess(file, fileList) {
				this.photos += file.data + ",";
			},
			// 返回上一级
			prev() {
				// this.$router.back()
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
			//台球馆套餐
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
					let returnData = data.data;
					this.statusmain = returnData
				})
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
			// 发布充电桩
			artiReleass() {
				// let photost = this.photos;
				let storeId = this.$route.query.storeId
				let merchantId = this.$route.query.merchantId
				// photost = photost.substr(0, photost.length - 1);
				// if (this.goodsCategory == 1) {
					if (this.title == "") {
						this.$notify({
							title: "提示",
							duration: 1800,
							message: "请输入充电桩名称",
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
				// 			message: "请输入棋牌桌名称",
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
				// 			message: "请输入柜子名称",
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
				
				

				// if (this.hourPrice == "") {
				// 	this.$notify({
				// 		title: "提示",
				// 		duration: 1800,
				// 		message: "请输入小时价",
				// 		type: "warning",
				// 	});
				// 	return;
				// }
				// if (this.minutePrice == "") {
				// 	this.$notify({
				// 		title: "提示",
				// 		duration: 1800,
				// 		message: "请输入分钟价",
				// 		type: "warning",
				// 	});
				// 	return;
				// }
				// if (this.cashPledge == "") {
				// 	this.$notify({
				// 		title: "提示",
				// 		duration: 1800,
				// 		message: "请输入押金",
				// 		type: "warning",
				// 	});
				// 	return;
				// }
				// if (this.subscription == "") {
				// 	this.$notify({
				// 		title: "提示",
				// 		duration: 1800,
				// 		message: "请输入预约价格",
				// 		type: "warning",
				// 	});
				// 	return;
				// }
				// if (this.goods_price < 1) {
				// 	this.$notify({
				// 		title: "提示",
				// 		duration: 1800,
				// 		message: "充电桩价格不能小于1元",
				// 		type: "warning",
				// 	});
				// 	return;
				// }
				// if (this.door_pwd == "") {
				// 	this.$notify({
				// 		title: "提示",
				// 		duration: 1800,
				// 		message: "请输入开门密码",
				// 		type: "warning",
				// 	});
				// 	return;
				// }
				// if (this.room_num == '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请输入房间号码',
				// 		type: 'warning'
				// 	});
				// 	return
				// }
				// if (this.user_time === '') {
				//   this.$notify({
				//     title: '提示',
				//     duration: 1800,
				//     message: '请输入有效时间',
				//     type: 'warning'
				//   });
				//   return
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
				// if (this.channels === "") {
				// 	this.$notify({
				// 		title: "提示",
				// 		duration: 1800,
				// 		message: "请输入子设备下的通道号",
				// 		type: "warning",
				// 	});
				// 	return;
				// }
				// if (this.tcitem.length == 0) {
				// 	this.$notify({
				// 		title: "提示",
				// 		duration: 1800,
				// 		message: "请选择充电桩套餐",
				// 		type: "warning",
				// 	});
				// 	return;
				// // }
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
				// 		message: "请上传桌台/柜子图标",
				// 		type: "warning",
				// 	});
				// 	return;
				// }

				// if (this.goodsType == "") {
				// 	this.$notify({
				// 		title: "提示",
				// 		duration: 1800,
				// 		message: "请输入桌台/柜子信息",
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
				this.$http({
					url: this.$http.adornUrl("goods/insertGoods"),
					method: "post",
					data: this.$http.adornData({
						'tearoomId': storeId,
						'goodsName': this.title,
						// 'goodsType': this.goods_attr,
						// 'price': this.goods_price,
						// 'roomPwd': this.door_pwd,
						'goodsCoverImage': this.goods_image,
						'goodsDetails': this.content,
						'goodsTitleImage': this.goodsTitleImage,
						'iconImg': this.iconImg,
						// 'endTime': this.user_time,
						'goodsTips': this.warm_reminder,
						'cameraId': this.cameraId,
						// 'roomWifi': this.wifi_pwd,
						// 'productIds':this.tcitem.toString(),//充电桩套餐id
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
								this.title = "";
								this.type = "";
								this.merchants = "";
								this.goods_price = "";
								this.door_pwd = "";
								this.goods_attr = "";
								this.content = "";
								this.sales = '';
								this.postagePrice = "";
								this.store_type_id = "";
								this.hourPrice = "";
								this.minutePrice = "";
								this.cashPledge = "";
								this.subscription = "";
								this.yyToken = "";
								this.yyId = "";
								this.sort = ''


								this.tableDataLoading = false
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
								// this.$router.push({
								// 	path: '/merchIncome',
								// 	query: {
								// 		storeId: storeId,
								// 		merchantId: merchantId,
								// 		activeName: 'shop'
								// 	}
								// });
							}
						})
					}

				});
			},
			select(item) {
				this.item.push(item)
				var goodsid = []
				this.item.forEach(index => {
					goodsid.push(index.productId)
				})
				var d = goodsid.toLocaleString();
				this.goods_cate_id = d
				console.log(this.goods_cate_id)
			},
			dataSelect1(){
				this.goodsCategory = this.$route.query.goodsCategory
				this.tearoomType = this.$route.query.tearoomType
				// this.init()
				// this.getshoptc()
			},



		},
		mounted() {
			this.goodsCategory = Number(this.$route.query.goodsCategory) 
			this.tearoomType = this.$route.query.tearoomType
			// this.getshoptc()
		},
		watch: {
		  '$route': 'dataSelect1'
		}

	};
</script>

<style scoped="scoped">
	.shop-publish2-page {
		overflow-x: hidden;
		padding-bottom: 16px;
	}

	.shop-publish2-page__back {
		margin-bottom: 12px;
	}

	.shop-publish2-page__back-link {
		display: inline-flex;
		align-items: center;
		font-size: 14px;
		color: #606266;
		text-decoration: none;
	}

	.shop-publish2-page__back-link:hover {
		color: #5cc477;
	}

	.shop-publish2-page__back-icon {
		width: 1.2em;
		height: 1.2em;
		margin-right: 4px;
	}

	.shop-publish2-page__form-body .el-col {
		margin-bottom: 4px;
		font-size: 14px;
		color: #606266;
	}

	.shop-publish2-page__field-label {
		display: inline-block;
		text-align: left;
		width: 100px;
		font-size: 14px;
		color: #606266;
	}

	.shop-publish2-page__field-input {
		width: 50%;
	}

	.shop-publish2-page__actions {
		display: flex;
		justify-content: center;
		align-items: center;
		gap: 16px;
		margin-bottom: 0;
	}

	.shop-publish2-page__actions .el-button + .el-button {
		margin-left: 0;
	}

	.shop-publish2-page >>> .el-radio__input.is-checked .el-radio__inner {
		border-color: #5cc477;
		background: #5cc477;
	}

	.shop-publish2-page >>> .el-radio__input.is-checked + .el-radio__label {
		color: #5cc477;
	}

	.tinymce-container {
		position: relative;
		line-height: normal;
	}

	.tinymce-container>>>.mce-fullscreen {
		z-index: 10000;
	}

	.tinymce-textarea {
		visibility: hidden;
		z-index: -1;
	}

	.hide .el-upload--picture-card {
		display: none;
	}

	.margin15 {
		margin-right: 15px;
		margin-top: 10px;
		display: inline-block;
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

	.el-tag+.el-tag {
		margin-left: 10px;
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
		border-color: #dcdfe6;
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

	.el-tag--medium {
		margin-right: 10px;
	}

	/* ::v-deep .el-radio-group {
		display: inline-flex;
		position: relative;
		left: 50%;
		top: -16px;
	} */

	::v-deep .el-checkbox-group {
		display: inline-flex;
		position: relative;
		left: -3px;
		top: 7px;
	}
</style>