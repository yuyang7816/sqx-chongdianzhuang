<template>
	<div class="nf-page shop-amend-s-page">
		<div class="shop-amend-s-page__back">
			<a href="#" @click.prevent="prev" class="shop-amend-s-page__back-link">
				<icon-svg name="jiantou" class="shop-amend-s-page__back-icon"></icon-svg>
				返回
			</a>
		</div>

		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">门店餐饮</span>
			<h2 class="nf-page-hero__title">修改商品</h2>
			<p class="nf-page-hero__desc">编辑外卖商品基本信息、图片、规格与详情，保存后返回商品列表。</p>
			<div class="nf-page-hero__bar"></div>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">
				<i class="el-icon-edit nf-filter-card__head-icon"></i>
				<span>基本信息</span>
			</div>
			<div class="shop-amend-s-page__form-body">
			<el-row>
				<el-col :span="8">
					<span>商品名称：</span>
					<el-input style="width: 200px;" class="margin15" placeholder="请输入商品标题" v-model="goodsName"
						autosize></el-input>
				</el-col>
				<el-col :span="8">
					<span>商品分类：</span>
					<el-select v-model="classifyId" class="margin15" placeholder="请选择商品类型" style="width:200px;"
						@change="handleChange1">
						<el-option v-for="(item,index) in typeDatas" :key="index" :label="item.classifyName"
							:value="item.classifyId">
						</el-option>
					</el-select>
				</el-col>
				<!-- <el-col :span="8">
          <span>商品原价：</span>
          <el-input style="width:200px;" class="margin15" placeholder="请输入商品原价" type="number" :min="0" :controls="false"
            v-model="originalMoney" autosize></el-input>
        </el-col> -->
				<el-col :span="8">
					<span>折扣价：</span>
					<el-input style="width:200px;" class="margin15" placeholder="请输入折扣价" type="number" :min="0"
						:controls="false" v-model="goodsMoney" autosize></el-input>
				</el-col>
				<el-col :span="8">
					<span>打包费：</span>
					<el-input style="width:200px;" class="margin15" placeholder="请输入打包费" type="number" :min="0"
						:controls="false" v-model="packMoney" autosize></el-input>
				</el-col>
				<!-- <el-col :span="8">
          <span>商品销量：</span>
          <el-input style="width:200px;" class="margin15" placeholder="请输入商品销量" type="number" :min="0" :controls="false"
            v-model="salesVolume" autosize></el-input>
        </el-col> -->
				<el-col :span="8">
					<span>商品状态：</span>
					<el-select v-model="status" class="margin15" placeholder="请选择商品状态" style="width:200px;">
						<el-option v-for="(item,index) in statusmain2" :key="item.index" :label="item.label"
							:value="item.value">
						</el-option>
					</el-select>
				</el-col>
				<el-col :span="8">
					<span>商品简介：</span>
					<el-input style="width:200px;" class="margin15" placeholder="请输入商品简介" type="text" :controls="false"
						v-model="goodsSynopsis" autosize></el-input>
				</el-col>
				<el-col :span="8" v-if="shopId!=0||shopId!==''">
					<span>商品库存：</span>
					<el-input style="width:200px;" class="margin15" placeholder="请输入商品库存" type="text" :controls="false"
						v-model="inventory" autosize></el-input>
				</el-col>

			</el-row>
			<el-row>
				<el-col :span="24">
					<div class="shop-amend-s-page__tags-row">
						<span>商品标签：</span>
						<span class="bqList" v-for="(item,index) in labels" :key="index">{{item}}
							<span class="delss">
								<i class="el-icon-delete" @click="dels1(index)"></i>
							</span>
						</span>
						<el-input v-model="bq" style="width:200px;" placeholder="请输入标签"></el-input>
						<el-button type="primary" class="tj" size="mini" icon="el-icon-edit" circle @click="btnTj">
						</el-button>
					</div>
				</el-col>
			</el-row>
			</div>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">
				<i class="el-icon-picture-outline nf-filter-card__head-icon"></i>
				<span>商品图片</span>
			</div>
			<div class="shop-amend-s-page__upload-row">
				<span class="shop-amend-s-page__field-label">商品封面图：</span>
				<div class="shop-amend-s-page__upload-box shop-amend-s-page__upload-box--cover">
					<el-upload class="avatar-uploader" v-model="goodsCover" :action="$http.adornUrlUp('alioss/upload')"
						:show-file-list="false" :on-success="handleAvatarSuccess1">
						<img v-if="goodsCover" :src="goodsCover" class="avatar" style="width: 148px;height: 148px;" />
						<i v-else class="el-icon-plus avatar-uploader-icon" style="font-size: 28px;color: #8c939d"></i>
					</el-upload>
				</div>
			</div>
			<div class="shop-amend-s-page__upload-row shop-amend-s-page__upload-row--wrap">
				<span class="shop-amend-s-page__field-label">商品轮播图：</span>
				<div v-for="(item,index) of imgs" :key="index">
					<div class="shop-amend-s-page__carousel-item">
						<img :src="item" class="avatar" style="width:148px;height: 148px;" />
						<div @click="clear(index)" class="divhove">
							<i class="el-icon-delete" style="margin: 0;font-size: 18px;color: #fff;"></i>
						</div>
					</div>
				</div>
				<div>
					<el-upload :action="$http.adornUrlUp('alioss/upload')" list-type="picture-card"
						:on-success="handleUploadSuccess" :on-change="handleChange" :on-remove="handleRemove">
						<i class="el-icon-plus"></i>
					</el-upload>
				</div>
			</div>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">
				<i class="el-icon-s-grid nf-filter-card__head-icon"></i>
				<span>商品规格</span>
			</div>
			<div class="shop-amend-s-page__spec-row">
				<span class="shop-amend-s-page__field-label">商品规格：</span>
				<el-radio-group v-model="guige" @change="guigeChange">
					<el-radio :label="0">单规格</el-radio>
					<el-radio :label="1">多规格</el-radio>
				</el-radio-group>
				<div v-if="guigeshow" class="shop-amend-s-page__spec-select">
					<el-select v-model="ruleId" placeholder="请选择商品规格" style="width:200px;"
						@change="selectTrigger(ruleId)">
						<el-option v-for="(item,index) in specifdata" :key="item.index" :label="item.ruleName"
							:value="item.id">
						</el-option>
					</el-select>
				</div>

			</div>
			<div v-if="ruleValue" class="shop-amend-s-page__rule-block">
				<div class="ruleitem" v-for="(item,index) in ruleValue" :key="index">
					<div>{{item.value}} <span @click="deleterule(index)">
							<icon-svg name="shanchu" class="ruleicon"></icon-svg>
						</span></div>
					<div>
						<el-tag :key="tag" v-for="(tag,j) in item.detail.split(',')" closable
							:disable-transitions="false" @close="handleClose(tag,j,item)">
							{{tag}}
						</el-tag>
						<el-input class="input-new-tag" v-model="inputValues[index]" ref="saveTagInput" size="small"
							style="width:120px;" placeholder="请输入属性名"
							@keyup.enter.native="handleInputConfirm(index,item)">
						</el-input>
						<el-button class="button-new-tag" size="small"
							@click="handleInputConfirm(index,item)">添加</el-button>
					</div>
				</div>
				<div class="shop-amend-s-page__rule-actions" v-if="ruleshow">
					<div v-if="isshow" class="btn_specif">
						<el-button type="primary" @click="btnToclick()">添加新规格</el-button>
						<el-button type="success" @click="create()">立即生成</el-button>
					</div>
				</div>
				<div class="shop-amend-s-page__rule-form">
					<div v-if="!isshow" class="shop-amend-s-page__rule-form-inner">
						<div class="shop-amend-s-page__inline-field">
							<span class="shop-amend-s-page__inline-label">规格：</span>
							<el-input style="width:50%;" v-model="releobject.value" placeholder="例:颜色"></el-input>
						</div>
						<div class="shop-amend-s-page__inline-field">
							<span class="shop-amend-s-page__inline-label">规格值：</span>
							<el-input style="width:50%;" v-model="releobject.detail" placeholder="例:黑色"></el-input>
						</div>
						<div class="shop-amend-s-page__inline-field shop-amend-s-page__inline-field--actions">
							<el-button type="primary" @click="speciTo()">确 定</el-button>
							<el-button @click="speciTotwo()">取 消</el-button>
						</div>
					</div>
				</div>
			</div>
		</div>

		<div class="nf-table-card shop-amend-s-page__attr-card" v-if="valueshow">
			<div class="nf-filter-card__head">
				<i class="el-icon-goods nf-filter-card__head-icon"></i>
				<span>商品属性</span>
			</div>
			<div class="shop-amend-s-page__attr-wrap">
				<div class="property_table">
					<table style="width: 100%;">
						<tr style="display:flex;text-align: left;">
							<th style="width:120px;" v-for="(item,index) in headerData" :key="index">{{item}}</th>
						</tr>
					</table>
					<el-table :data="valueData">
						<el-table-column v-if="detailJson.length == 1" prop="value0" width="120">
						</el-table-column>
						<el-table-column v-if="detailJson.length == 2" prop="value0" width="120">
						</el-table-column>
						<el-table-column v-if="detailJson.length == 2" prop="value1" width="120">
						</el-table-column>
						<el-table-column v-if="detailJson.length == 3" prop="value0" width="120">
						</el-table-column>
						<el-table-column v-if="detailJson.length == 3" prop="value1" width="120">
						</el-table-column>
						<el-table-column v-if="detailJson.length == 3" prop="value2" width="120">
						</el-table-column>
						<el-table-column v-if="detailJson.length == 4" prop="value0" width="120">
						</el-table-column>
						<el-table-column v-if="detailJson.length == 4" prop="value1" width="120">
						</el-table-column>
						<el-table-column v-if="detailJson.length == 4" prop="value2" width="120">
						</el-table-column>
						<el-table-column v-if="detailJson.length == 4" prop="value3" width="120">
						</el-table-column>
						<el-table-column v-if="detailJson.length == 5" prop="value0" width="120">
						</el-table-column>
						<el-table-column v-if="detailJson.length == 5" prop="value1" width="120">
						</el-table-column>
						<el-table-column v-if="detailJson.length == 5" prop="value2" width="120">
						</el-table-column>
						<el-table-column v-if="detailJson.length == 5" prop="value3" width="120">
						</el-table-column>
						<el-table-column v-if="detailJson.length == 5" prop="value4" width="120">
						</el-table-column>
						<el-table-column prop="skuImg" width="100">
							<template slot-scope="scope">
								<div class="imgWrap"
									style=" width:60px;height:60px;background-color: #fbfdff; border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 60px;">
									<el-upload style="width: 60px;height: 60px;" class="avatar-uploader"
										v-model="scope.row.skuImg" :action="$http.adornUrlUp('alioss/upload')"
										:show-file-list="false" :on-success="handleAvatarSuccess">
										<img v-if="scope.row.skuImg" :src="scope.row.skuImg" class="avatar"
											style="border-radius: 6px;width:60px;height: 60px;" />
										<i v-else class="el-icon-plus avatar-uploader-icon"
											@click="curRowIndex=scope.$index"></i>
									</el-upload>
								</div>
							</template>
						</el-table-column>
						<!--            <el-table-column prop="skuOriginalPrice" width="120">
              <template slot-scope="scope">
                <div>
                  <el-input type="text" v-model="scope.row.skuOriginalPrice" style="width:80px;font-size:14px;">
                  </el-input>
                </div>
              </template>
            </el-table-column> -->
						<el-table-column prop="skuPrice" width="120">
							<template slot-scope="scope">
								<div>
									<el-input type="text" v-model="scope.row.skuPrice"
										style="width:80px;font-size:14px;">
									</el-input>
								</div>
							</template>
						</el-table-column>
						<!-- <el-table-column prop="sales" width="120">
                <template slot-scope="scope">
                  <div>
                    <el-input type="text" v-model="scope.row.sales" style="width:80px;font-size:14px;">
                    </el-input>
                  </div>
                </template>
              </el-table-column> -->
						<el-table-column width="120">
							<template slot-scope="scope">
								<el-button :disabled="!isAuth('userList:delete')" size="mini" type="danger"
									@click="deleter(scope.$index)">删除
								</el-button>
							</template>
						</el-table-column>
					</el-table>
				</div>
			</div>
		</div>

		<div class="nf-table-card shop-amend-s-page__attr-card" v-if="valueshow1">
			<div class="nf-filter-card__head">
				<i class="el-icon-goods nf-filter-card__head-icon"></i>
				<span>商品属性</span>
			</div>
			<div class="shop-amend-s-page__attr-wrap">
				<div class="property_table">
					<table style="width: 100%;">
						<tr style="display:flex;text-align: left;">
							<th style="width:150px;" v-for="(item,index) in headerData2" :key="index">{{item}}</th>
						</tr>
					</table>
					<el-table :data="valueData2">
						<el-table-column prop="skuImg" width="150">
							<template slot-scope="scope">
								<div class="imgWrap"
									style=" width:60px;height:60px;background-color: #fbfdff; border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 60px;">
									<el-upload style="width: 60px;height: 60px;" class="avatar-uploader"
										v-model="scope.row.skuImg" :action="$http.adornUrlUp('alioss/upload')"
										:show-file-list="false" :on-success="handleAvatarSuccess3">
										<img v-if="scope.row.skuImg" :src="scope.row.skuImg" class="avatar"
											style="border-radius: 6px;width:60px;height: 60px;" />
										<i v-else class="el-icon-plus avatar-uploader-icon"
											@click="curRowIndex=scope.$index"></i>
									</el-upload>
								</div>
							</template>
						</el-table-column>
						<!-- <el-table-column prop="skuOriginalPrice" width="150">
              <template slot-scope="scope">
                <div>
                  <el-input type="text" v-model="scope.row.skuOriginalPrice" style="width:80px;font-size:14px;">
                  </el-input>
                </div>
              </template>
            </el-table-column> -->
						<el-table-column prop="skuPrice" width="150">
							<template slot-scope="scope">
								<div>
									<el-input type="text" v-model="scope.row.skuPrice"
										style="width:80px;font-size:14px;">
									</el-input>
								</div>
							</template>
						</el-table-column>
						<!-- <el-table-column prop="sales" width="150">
                <template slot-scope="scope">
                  <div>
                    <el-input type="text" v-model="scope.row.sales" style="width:80px;font-size:14px;">
                    </el-input>
                  </div>
                </template>
              </el-table-column> -->
					</el-table>
				</div>
			</div>
		</div>

		<div class="nf-table-card shop-amend-s-page__detail-card">
			<div class="nf-filter-card__head">
				<i class="el-icon-document nf-filter-card__head-icon"></i>
				<span>商品详情</span>
			</div>
			<div class="shop-amend-s-page__editor-wrap">
				<quill-editor ref="myTextEditor" v-model="goodsDescribe" :options="quillOption"
					class="shop-amend-s-page__editor">
				</quill-editor>
			</div>
		</div>

		<div class="nf-filter-card shop-amend-s-page__actions">
			<el-button type="primary" icon="el-icon-back" @click="prev">返回上一页</el-button>
			<el-button type="primary" icon="el-icon-check" @click="artiReleass">保存</el-button>
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

	export default {
		components: {
			quillEditor
		},
		name: 'Tinymce',
		data() {
			return {
				originalPrice: '',
				price: '',
				sales: '',
				imgs: [],
				value: [],
				potost: [],
				statusmain2: [{
						value: 0,
						label: '上架'
					},
					{
						value: 1,
						label: '下架'
					}
				],
				valueshow: true,
				valueshow1: true,
				// 富文本内容
				descrition: '',
				// curRowIndex: null,

				// ···········································
				specif: '',
				goodsId: '',
				goodsName: '',
				classifyId: '',
				merchants: '',
				originalMoney: '',
				goodsMoney: '',
				packMoney: '',
				memberPrice: '',
				labels: [], //标签集合
				bq: '', //输入的标签名
				createTime: '',
				positage: 0,
				guige: 1,
				skuImg: '',
				ruleId: '',
				postagePrice: '',
				priceshow: false,
				isshow: true,
				commissionPrice: '',
				buyReason: '',
				ruleshow: false,
				propshow: false,
				guigeshow: true,
				propshow1: false,
				ruleValue: [],
				attr: [],
				attrs: [],
				headerData: [],
				valueData: [],
				detailJson: [],
				inputValues: {
					inputValue0: '',
					inputValue1: '',
					inputValue2: '',
					inputValue3: '',
					inputValue4: '',
					inputValue5: ''
				},
				sku: [],
				img: '',
				salesVolume: '',
				goodsSynopsis: '',
				inventory: '',
				status: 0,
				goodsPicture: '',
				typeDatas: [],
				value: [],
				isSelect: 1,
				homeGoods: 1,
				isRecommend: 1,
				isExpress: '',
				goodsCover: '',
				valueData2: [],
				headerData2: [],

				isExpressmain: [{
						value: 1,
						label: '普通商品需要发货'
					},
					{
						value: 2,
						label: '虚拟商品无需发货'
					},
					{
						value: 3,
						label: '门店核销无需发货'
					}
				],
				attrName: '',
				ruleName: '',
				attrValue: [],
				releobject: {
					value: '',
					detail: ''
				},
				brandId: '',
				brandIds: [],
				specifdata: [],
				dialogVisible: false,
				isShow: false,
				hideUpload: false,
				limit: 1,
				id: '',
				hasChange: false,
				hasInit: false,
				tinymceId: 'tinymceId',
				height: 435,
				toolbar: [],
				menubar: 'file edit insert view format table',
				languageTypeList: {
					'en': 'en',
					'zh': 'zh_CN'
				},
				artiCletypes: [],
				// 富文本内容
				quillOption: quillConfig,
				goodsDescribe: '',
				shopId: '',
			}
		},
		methods: {
			// 返回上一级
			prev() {
				this.$router.back()
			},
			// 图标上传
			handleAvatarSuccess(file) {
				if (this.curRowIndex == 0) {
					this.skuImg = file.data;
					this.valueData[0].skuImg = this.skuImg
					return
				} else {
					this.skuImg = file.data;
					this.valueData[this.curRowIndex].skuImg = this.skuImg
				}
			},
			// 图标上传
			handleAvatarSuccess3(file) {
				this.skuImg = file.data;
				this.sku[0].skuImg = this.skuImg
			},
			// 是否包邮
			agreeChange(val) {
				if (val == 1) {
					this.priceshow = true
					this.postagePrice = ''
				} else {
					this.priceshow = false
					this.postagePrice = 0
				}
			},
			// 是否多规格
			guigeChange(val) {
				if (val == 0) {
					this.ruleId = 0
					this.valueshow = false
					this.valueshow1 = true
					this.guigeshow = false
					this.attr = []
					this.ruleshow = false
					this.ruleValue = []
					this.dansku()
				}
				if (val == 1) {
					this.ruleId = ''
					this.sku = []
					this.guigeshow = true
					this.valueshow1 = false
				}
			},
			// 获取单规格的sku
			dansku() {
				// if (this.originalMoney == '') {
				//   this.originalMoney = '0'
				// }
				if (this.goodsMoney == '') {
					this.goodsMoney = '0'
				}
				this.$http({
					url: this.$http.adornUrl('admin/takeoutGoods/onlyFormatAttr'),
					method: 'get',
					params: this.$http.adornParams({
						// 'originalPrice': this.originalMoney,
						'price': this.goodsMoney,
						'coverImg': this.goodsCover,
					})
				}).then(({
					data
				}) => {
					let returnData = data.data;
					this.valueData2 = returnData.value
					this.headerData2 = returnData.header
					this.sku = returnData.value
					this.valueshow1 = true
					this.valueshow = false
					this.guigeshow = false
				})
			},
			// 回显规格
			echo(goodsId) {
				this.$http({
					url: this.$http.adornUrl('admin/takeoutGoods/formatAttr'),
					method: 'get',
					params: this.$http.adornParams({
						'goodsId': goodsId,
					})
				}).then(({
					data
				}) => {
					let returnData = data.data;
					this.valueData2 = returnData.value
					this.headerData2 = returnData.header
					this.valueshow1 = true
					this.valueshow = false
					this.guigeshow = false
				})
			},
			handleChange1(value) {
				this.classifyId = value;
			},
			// 删除
			clear(index) {
				this.imgs.splice(index, 1);
			},
			handleChange(file, fileList) {
				this.hideUpload = fileList.length >= this.limit;
			},
			handleRemove(file, fileList) {
				this.hideUpload = fileList.length >= this.limit;
				for (var i in this.potost) {
					if (this.potost[i] == file.response.data) {
						this.potost.splice(i, 1);
					}
				}
			},
			handleAvatarSuccess1(file, fileList) {
				this.goodsCover = file.data
			},
			//上传成功
			handleUploadSuccess(file, fileList) {
				console.log('file.data:', file.data, 'this.goodsPicture', this.goodsPicture)
				// this.potost += file.data + ','
				this.potost.push(file.data)
			},
			init() {


				let shopId = this.$route.query.shopId

				console.log('======111======', shopId)
				let goodsId = this.$route.query.goodsId
				this.$http({
					url: this.$http.adornUrl('admin/takeoutGoods/selectGoodsById'),
					method: 'get',
					params: this.$http.adornParams({
						'goodsId': goodsId,
						'shopId': shopId
					})
				}).then(({
					data
				}) => {
					let returnData = data.data;
					if (returnData.goodsPicture) {
						this.imgs = returnData.goodsPicture.split(',')
					} else {
						this.imgs = []
					}
					// this.imgs = imgs;
					if (returnData.goodsLabel && returnData.goodsLabel != '') {
						let goodsLabel = returnData.goodsLabel.split(',')
						this.labels = goodsLabel
					}else{
						this.labels =[]
					}

					this.goodsName = returnData.goodsName
					this.goodsId = returnData.goodsId;

					console.log('this.imgs', this.imgs)
					// this.goodsPicture = goodsPicture;
					this.goodsCover = returnData.goodsCover

					this.goodsMoney = returnData.goodsMoney
					this.originalMoney = returnData.originalMoney;
					this.packMoney = returnData.packMoney
					this.salesVolume = returnData.salesVolume;
					this.classifyId = returnData.classifyId;
					this.goodsDescribe = returnData.goodsDescribe;
					this.goodsSynopsis = returnData.goodsSynopsis
					if (returnData.inventory) {
						this.inventory = returnData.inventory
					} else {
						this.inventory = ''
					}
					this.status = returnData.status;
					this.createTime = returnData.createTime;
					if (returnData.shopId == '' || returnData.shopId == 0) {
						this.shopId = ''
					} else {
						this.shopId = returnData.shopId
					}


					// this.memberPrice = returnData.memberPrice;
					// this.descrition = returnData.descrition;
					// this.status = returnData.status;
					// this.createAt = returnData.createAt;
					// this.isSelect = returnData.isSelect;
					// this.homeGoods = returnData.homeGoods;
					// this.isRecommend = returnData.isRecommend;
					// this.isExpress = returnData.isExpress;
					// this.createAt = returnData.createAt;
					// this.brandId = returnData.brandId;
					// let label = JSON.parse(returnData.type.parentId)
					// let value = JSON.parse(returnData.type.id)
					// this.value = [label, value]
					this.postagePrice = returnData.postagePrice

					if (this.postagePrice > 0) {
						this.positage = 1
						this.priceshow = true
					} else {
						this.positage = 0
						this.priceshow = false
					}

					this.attr = returnData.attr
					if (this.attr.length > 0) {
						this.guige = 1
						this.Triggerselect()
						this.create2()
						this.attrName = returnData.attr[0].attrName
						this.attrValue = returnData.attr[0].attrValue
						this.ruleId = returnData.attr[0].ruleId
						this.goodsId = returnData.attr[0].goodsId
						this.id = returnData.attr[0].id
					} else {
						this.guige = 0
						this.echo(this.goodsId)
						this.ruleId = 0
						this.sku = returnData.sku
					}
				})
			},
			// 修改商品
			artiReleass() {
				// let id = this.$route.query.id
				console.log('this.guige', this.guige)

				var potost = this.potost.toString()
				if (this.imgs == '') {
					this.img = this.potost
					// this.img = this.img.substr(0, this.img.length - 1)
				} else {
					if (this.potost.length != 0) {
						this.img = this.imgs + ',' + this.potost
					} else {
						this.img = this.imgs
						// this.img = this.img.substr(0, this.img.length - 1)
					}

				}
				if (this.labels.length == 0) {
				  this.$notify({
				    title: '提示',
				    duration: 1800,
				    message: '标签不能为空',
				    type: 'warning'
				  });
				  return
				}
				console.log('this.imgs', this.imgs)
				console.log('this.img', this.img)
				console.log('this.potost:', this.potost)
				// let photost = this.goodsPicture.toString()
				let goodsLabel = this.labels.toString()
				if (this.goodsName == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入商品标题',
						type: 'warning'
					});
					return
				}
				if (this.classifyId === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择商品类型',
						type: 'warning'
					});
					return
				}
				// if (this.originalMoney === '') {
				//   this.$notify({
				//     title: '提示',
				//     duration: 1800,
				//     message: '请输入商品原价',
				//     type: 'warning'
				//   });
				//   return
				// }
				if (this.goodsMoney === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入折扣价',
						type: 'warning'
					});
					return
				}
				if (this.packMoney === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入打包费',
						type: 'warning'
					});
					return
				}

				// if (this.salesVolume === '') {
				//   this.$notify({
				//     title: '提示',
				//     duration: 1800,
				//     message: '请输入商品销量',
				//     type: 'warning'
				//   });
				//   return
				// }
				if (this.goodsCover == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择商品封面图',
						type: 'warning'
					});
					return
				}
				if (this.goodsDescribe == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入商品详情',
						type: 'warning'
					});
					return
				}
				if (this.sku.length == 0) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择商品规格',
						type: 'warning'
					});
					return
				}
				if (this.guige == 0) {
					this.sku = this.valueData2
				} else {
					this.sku = this.valueData
				}
				this.$http({
					url: this.$http.adornUrl('admin/takeoutGoods/update'),
					method: 'post',
					data: this.$http.adornData({
						'goodsId': this.goodsId,
						'goodsPicture': this.img.toString(),
						'goodsName': this.goodsName,
						'classifyId': this.classifyId,
						'originalMoney': this.originalMoney,
						'goodsMoney': this.goodsMoney,
						'packMoney': this.packMoney,
						'memberPrice': this.memberPrice,
						'salesVolume': this.salesVolume,
						'goodsSynopsis': this.goodsSynopsis,
						'inventory': this.inventory,
						'status': this.status,
						'goodsCover': this.goodsCover,
						'goodsDescribe': this.goodsDescribe,
						'postagePrice': this.postagePrice,
						'sku': this.sku,
						'attr': this.attr,
						'goodsLabel': goodsLabel,
						'createTime': this.createTime,
						'shopId': this.shopId
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.$router.back()
							}
						})
					} else {
						this.$message({
							message: data.msg,
							type: 'warning',
							duration: 1500,
							onClose: () => {}
						})
					}

				})
			},

			// 商品规格
			specifSelect() {
				this.$http({
					url: this.$http.adornUrl('selfGoodsRule/list'),
					method: 'get',
					params: this.$http.adornParams({
						'shopId': this.shopId
					})
				}).then(({
					data
				}) => {
					let returnData = data.data;
					this.specifdata = returnData;
				})
			},
			// 商品规格
			Triggerselect() {
				let goodsId = this.$route.query.goodsId
				this.$http({
					url: this.$http.adornUrl('admin/takeoutGoods/findAttrValue'),
					method: 'get',
					params: this.$http.adornParams({
						'goodsId': goodsId
					})
				}).then(({
					data
				}) => {
					let returnData = data.data;
					this.ruleValue = returnData.attrValue
					this.attrName = returnData.attrName
					this.ruleshow = true
				})
			},
			// 删除商品属性
			deleter(index) {
				let length = this.sku.length
				if (length == 1) {
					this.$message({
						title: '提示',
						type: 'error',
						duration: 1800,
						message: '属性至少要有一个',
						type: 'warning'
					});
					return
				} else {
					this.sku.splice(index, 1);
				}
			},
			// 查询商品规格
			selectTrigger(val) {
				this.$http({
					url: this.$http.adornUrl('selfGoodsRule/find'),
					method: 'get',
					params: this.$http.adornParams({
						'id': val
					})
				}).then(({
					data
				}) => {
					let returnData = data.data;
					this.ruleValue = returnData.ruleValue
					this.attrName = returnData.ruleName
					this.ruleshow = true
					this.ruleId = returnData.id
					this.create()
				})
			},
			deleterule(index) {
				this.ruleValue.splice(index, 1);
			},
			btnToclick() {
				this.isshow = false
				this.releobject.value = ''
				this.releobject.detail = ''
			},
			speciTotwo() {
				this.isshow = true
				this.releobject.value = ''
				this.releobject.detail = ''
			},
			speciTo() {
				if (this.releobject.value == '') {
					this.$message({
						title: '提示',
						type: 'error',
						duration: 1800,
						message: '请输入规格',
						type: 'warning'
					});
					return
				}
				if (this.releobject.detail == '') {
					this.$message({
						title: '提示',
						duration: 1800,
						type: 'error',
						message: '请输入规格值',
						type: 'warning'
					});
					return
				}
				this.ruleValue.push({
					value: this.releobject.value,
					detail: this.releobject.detail
				});
				this.isshow = true
				this.releobject.value = ''
				this.releobject.detail = ''
			},
			handleClose(tag, j, item) {
				var detailarr = item.detail.split(',')
				detailarr.splice(detailarr.indexOf(tag), 1);
				if (detailarr.length < 1) {
					this.$message({
						title: '提示',
						type: 'error',
						duration: 1800,
						message: '规格值至少要有一个',
						type: 'warning'
					});
					return
				} else {
					item.detail = String(detailarr)
				}
			},
			handleInputConfirm(index, item) {
				let inputValue = this.inputValues[index];
				if (inputValue) {
					var detailarr = item.detail.split(',')
					detailarr.push(inputValue)
					for (var i = 0; i < detailarr.length; i++) {
						for (var j = 0; j < detailarr.length; j++) {
							if (detailarr[i] == detailarr[j] && i != j) {
								detailarr.splice(j, 1);
							}
						}
					}
					item.detail = String(detailarr)
					this.inputValues[index] = '';
				}
				this.inputVisible = false;
			},
			// 初始化立即生成
			create2() {
				let goodsId = this.$route.query.goodsId
				this.$http({
					url: this.$http.adornUrl('admin/takeoutGoods/formatAttr'),
					method: 'get',
					params: this.$http.adornParams({
						'goodsId': goodsId
					})
				}).then(({
					data
				}) => {
					let returnData = data.data;
					this.headerData = returnData.header
					this.valueData = returnData.value
					this.valueshow = true
					this.valueshow1 = false
					this.sku = returnData.value
					this.detailJson = returnData.value[0].detailJson.split(',')
				})
			},
			// 立即生成
			create() {
				this.attr = []
				this.attr.push({
					attrName: this.attrName,
					attrValue: this.ruleValue,
					ruleId: this.ruleId
				})
				// if (this.originalMoney == '') {
				//   this.originalMoney = '0'
				// }
				if (this.goodsMoney == '') {
					this.goodsMoney = '0'
				}
				if (this.memberPrice == '') {
					this.memberPrice = '0'
				}
				this.$http({
					url: this.$http.adornUrl(
						`admin/takeoutGoods/isFormatAttr?coverImg=${this.goodsCover}&price=${this.goodsMoney}`
					),
					method: 'post',
					data: this.attr[0]
				}).then(({
					data
				}) => {
					let returnData = data.data;
					this.headerData = returnData.header
					this.valueData = returnData.value
					this.valueshow = true
					this.valueshow1 = false
					this.sku = returnData.value
					this.detailJson = returnData.value[0].detailJson.split(',')
				})
			},
			// 获取品牌
			brandSelect() {
				let page = this.page - 1
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('selfGoodsBrand/result'),
					method: 'get',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.brandIds = returnData
				})
			},
			// 商品分类
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('admin/takeoutGoods/selectAllClassify'),
					method: 'get',
					params: this.$http.adornParams({
						'shopId': this.shopId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.typeDatas = returnData
				})
			},
			// 添加标签
			btnTj() {
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
					this.labels.push(this.bq)
					this.bq = ''
				}

			},
			// 删除标签
			dels1(index) {
				this.labels.splice(index, 1);
				console.log(this.labels)
			},
			dataSelect1() {
				this.init()
				this.dataSelect()
				this.specifSelect()
			}
		},
		mounted() {
			this.init()
			this.dataSelect()
			this.specifSelect()
			// this.brandSelect()
		},
		watch: {
			'$route': 'dataSelect1'
		}

	}
</script>

<style scoped>
.shop-amend-s-page {
	overflow-x: hidden;
	padding-bottom: 16px;
}

.shop-amend-s-page__back {
	margin-bottom: 12px;
}

.shop-amend-s-page__back-link {
	display: inline-flex;
	align-items: center;
	font-size: 14px;
	color: #606266;
	text-decoration: none;
}

.shop-amend-s-page__back-link:hover {
	color: #5cc477;
}

.shop-amend-s-page__back-icon {
	width: 1.2em;
	height: 1.2em;
	margin-right: 4px;
}

.shop-amend-s-page__form-body .el-col {
	margin-bottom: 4px;
	font-size: 14px;
	color: #606266;
}

.shop-amend-s-page__tags-row {
	display: flex;
	flex-wrap: wrap;
	align-items: center;
	gap: 8px;
	margin-top: 12px;
	font-size: 14px;
	color: #606266;
}

.shop-amend-s-page__field-label {
	flex: 0 0 auto;
	min-width: 100px;
	font-size: 14px;
	color: #606266;
}

.shop-amend-s-page__upload-row {
	display: flex;
	align-items: center;
	margin-bottom: 16px;
	gap: 12px;
}

.shop-amend-s-page__upload-row--wrap {
	flex-wrap: wrap;
	align-items: flex-start;
}

.shop-amend-s-page__upload-box {
	background-color: #fbfdff;
	border: 1px dashed #c0ccda;
	border-radius: 6px;
	text-align: center;
}

.shop-amend-s-page__upload-box--cover {
	width: 148px;
	height: 148px;
	line-height: 148px;
}

.shop-amend-s-page__carousel-item {
	position: relative;
	margin: 5px;
}

.shop-amend-s-page__spec-row {
	display: flex;
	align-items: center;
	flex-wrap: wrap;
	gap: 12px;
}

.shop-amend-s-page__spec-select {
	margin-left: 8px;
}

.shop-amend-s-page__rule-block {
	margin-top: 12px;
	padding-top: 12px;
	border-top: 1px solid #e8eaed;
}

.shop-amend-s-page__rule-actions {
	margin-top: 16px;
}

.shop-amend-s-page__rule-actions .el-button + .el-button {
	margin-left: 12px;
}

.shop-amend-s-page__rule-form-inner {
	display: flex;
	flex-wrap: wrap;
	align-items: center;
	gap: 12px;
	margin-top: 16px;
}

.shop-amend-s-page__inline-field {
	display: inline-flex;
	align-items: center;
	gap: 8px;
}

.shop-amend-s-page__inline-label {
	width: 80px;
	text-align: right;
	font-size: 14px;
	color: #606266;
}

.shop-amend-s-page__inline-field--actions .el-button + .el-button {
	margin-left: 8px;
}

.shop-amend-s-page__attr-wrap {
	overflow-x: auto;
}

.shop-amend-s-page__editor-wrap {
	width: 100%;
}

.shop-amend-s-page__editor {
	padding-bottom: 50px;
	height: 300px;
	width: 100%;
	display: inline-table;
	margin-bottom: 16px;
}

.shop-amend-s-page__actions {
	display: flex;
	justify-content: center;
	align-items: center;
	gap: 16px;
	margin-bottom: 0;
}

.shop-amend-s-page__actions .el-button + .el-button {
	margin-left: 0;
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

.bqList {
	padding: 4px 14px;
	margin: 4px;
	border: 1px solid #efefef;
	font-size: 12px;
	color: #999;
	border-radius: 4px;
	margin-right: 15px;
}

.delss {
	display: none;
	position: relative;
}

.delss .el-icon-delete {
	position: absolute;
	top: 0;
}

.bqList:hover .delss {
	display: initial;
	opacity: 0.5;
}

.tj {
	padding: 6px !important;
	margin: 4px;
	font-size: 12px;
	border: 1px solid #ccc;
	border-radius: 4px;
}
</style>