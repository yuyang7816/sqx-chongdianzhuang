<template>
	<div class="nf-page shop-amend-page">
		<div class="shop-amend-page__back">
			<a href="#" @click.prevent="prev" class="shop-amend-page__back-link">
				<icon-svg name="jiantou" class="shop-amend-page__back-icon"></icon-svg>
				返回
			</a>
		</div>

		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">商品管理</span>
			<h2 class="nf-page-hero__title">修改商品</h2>
			<p class="nf-page-hero__desc">编辑商品基本信息、图片、规格与详情，保存后返回商品列表。</p>
			<div class="nf-page-hero__bar"></div>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">
				<i class="el-icon-edit nf-filter-card__head-icon"></i>
				<span>基本信息</span>
			</div>
			<div class="shop-amend-page__form-body">
			<el-row>
				<el-col :span="6">
					<span>商品标题：</span>
					<el-input style="width: 200px;" class="margin15" placeholder="请输入商品标题" v-model="title" autosize>
					</el-input>
				</el-col>
				<el-col :span="6">
					<span>商品分类：</span>
					<el-cascader class="margin15" v-model="value" :options="typeDatas" placeholder="请选择商品分类"
						:show-all-levels="false" :change-on-select="true"
						:props="{ expandTrigger: 'hover',label:'name',value:'id' }" @change="handleChange1">
					</el-cascader>
				</el-col>
				<el-col :span="6">
					<span>商品原价：</span>
					<el-input style="width:200px;" class="margin15" placeholder="请输入商品原价" type="number" :min="0"
						:controls="false" v-model="originalPrice" autosize></el-input>
				</el-col>
				<el-col :span="6">
					<span>商品价格：</span>
					<el-input style="width:200px;" class="margin15" placeholder="请输入商品价格" type="number" :min="0"
						:controls="false" v-model="price" autosize></el-input>
				</el-col>
			</el-row>
			<el-row>
				<el-col :span="6">
					<span>会员价格：</span>
					<el-input style="width:200px;" class="margin15" placeholder="请输入会员价格" type="number" :min="0"
						:controls="false" v-model="memberPrice" autosize></el-input>
				</el-col>
				<el-col :span="6">
					<span>商品佣金：</span>
					<el-input style="width:200px;" class="margin15" placeholder="例0.1 等于10%" type="number" :min="0"
						:max="10" :controls="false" v-model="commissionPrice" autosize></el-input>
				</el-col>
				<el-col :span="6">
					<span>商品积分：</span>
					<el-input style="width:200px;" class="margin15" placeholder="请输入商品积分" type="number" :min="0"
						:max="10" :controls="false" v-model="integral" autosize></el-input>
				</el-col>

				<el-col :span="6">
					<span>商品销量：</span>
					<el-input style="width:200px;" class="margin15" placeholder="请输入商品销量" type="number" :min="0"
						:controls="false" v-model="sales" autosize></el-input>
				</el-col>
				<el-col :span="6">
					<span>商品状态：</span>
					<el-select v-model="status" class="margin15" placeholder="请选择商品类型" style="width:200px;">
						<el-option v-for="(item,index) in statusmain2" :key="index" :label="item.label"
							:value="item.value">
						</el-option>
					</el-select>
				</el-col>
			<!-- </el-row>
			<el-row> -->
				<el-col :span="6">
					<span>必买理由：</span>
					<el-input style="width:200px;" class="margin15" placeholder="请输入必买理由" v-model="buyReason" autosize>
					</el-input>
				</el-col>
				<el-col :span="6">
					<span>是否发货：</span>
					<el-select v-model="isExpress" class="margin15" placeholder="请选择商品是否发货" style="width:200px;">
						<el-option v-for="(item,index) in isExpressmain" :key="index" :label="item.label"
							:value="item.value">
						</el-option>
					</el-select>
				</el-col>
				<el-col :span="6">
					<span>商品品牌：</span>
					<el-select v-model="brandId" class="margin15" placeholder="请选择商品品牌" style="width:200px;">
						<el-option v-for="(item,index) in  brandIds" :key="index" :label="item.name" :value="item.id">
						</el-option>
					</el-select>
				</el-col>
				<el-col :span="6">
					<span>商品产地：</span>
					<el-input style="width: 200px" class="margin15" placeholder="请输入商品产地" v-model="chandi" autosize>
					</el-input>
				</el-col>
				<el-col :span="6">
					<span>导航归属：</span>
					<el-select v-model="nav_id" class="margin15" placeholder="请选择导航归属" style="width: 200px">
						<el-option v-for="(item, index) in navList" :key="index" :label="item.name" :value="item.id">
						</el-option>
					</el-select>
				</el-col>
				<el-col :span="6" style="margin-top: 20px;">
					<span>精选好物：</span>
					<el-radio-group v-model="isSelect">
						<el-radio :label="1">是</el-radio>
						<el-radio :label="0">否</el-radio>
					</el-radio-group>
				</el-col>
				<el-col :span="6" style="margin-top: 20px;">
					<span>首页商品：</span>
					<el-radio-group v-model="homeGoods">
						<el-radio :label="1">是</el-radio>
						<el-radio :label="0">否</el-radio>
					</el-radio-group>
				</el-col>
				</el-row>
				<el-row>
				<el-col :span="6" style="margin-top: 20px;">
					<span>每日推荐：</span>
					<el-radio-group v-model="isRecommend">
						<el-radio :label="1">是</el-radio>
						<el-radio :label="0">否</el-radio>
					</el-radio-group>
				</el-col>
			</el-row>
			<el-row>
				
				<el-col :span="24" style="margin-top: 20px;">
					<span>商品主题：</span>
					<!-- <el-select v-model="themeId" class="margin15" placeholder="请选择商品主题" style="width: 200px" multiple>
						<el-option v-for="(item, index) in themeList" :key="index" :label="item.name" :value="item.id">
						</el-option>
					</el-select> -->
					<el-checkbox-group v-model="themeId" class="margin15">
						<el-checkbox v-for="(item,index) in themeList" :key="index" :label="item.id">{{item.name}}
						</el-checkbox>
					</el-checkbox-group>
				</el-col>
			</el-row>
			</div>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">
				<i class="el-icon-picture-outline nf-filter-card__head-icon"></i>
				<span>商品图片</span>
			</div>
			<div class="shop-amend-page__upload-row">
				<span class="shop-amend-page__field-label">商品封面图：</span>
				<div class="shop-amend-page__upload-box shop-amend-page__upload-box--cover">
					<el-upload class="avatar-uploader" v-model="coverImg"
						:action="$http.adornUrlUp('alioss/upload')" :show-file-list="false"
						:on-success="handleAvatarSuccess1">
						<img v-if="coverImg" :src="coverImg" class="avatar" style="width: 140px;height: 140px;" />
						<i v-else class="el-icon-plus avatar-uploader-icon" style="font-size: 28px;color: #8c939d"></i>
					</el-upload>
				</div>
			</div>
			<div class="shop-amend-page__upload-row shop-amend-page__upload-row--wrap">
				<span class="shop-amend-page__field-label">商品轮播图：</span>
				<div v-for="(item,index) of imgs" :key="index">
					<div class="shop-amend-page__carousel-item">
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
			<div class="shop-amend-page__spec-row">
				<span class="shop-amend-page__field-label">商品规格：</span>
				<el-radio-group v-model="guige" @change="guigeChange">
					<el-radio :label="0">单规格</el-radio>
					<el-radio :label="1">多规格</el-radio>
				</el-radio-group>
				<div v-if="guigeshow" class="shop-amend-page__spec-select">
					<el-select v-model="ruleId" placeholder="请选择商品规格" style="width:200px;"
						@change="selectTrigger(ruleId)">
						<el-option v-for="(item,index) in specifdata" :key="index" :label="item.rule_name"
							:value="item.id">
						</el-option>
					</el-select>
				</div>
			</div>
			<div v-if="ruleValue" class="shop-amend-page__rule-block">
				<div class="ruleitem" v-for="(item,index) in ruleValue" :key="index">
					<div>{{item.rule_value}} <span @click="deleterule(index)">
							<icon-svg name="schu" class="ruleicon"></icon-svg>
						</span></div>
					<div>
						<el-tag :key="tag" v-for="(tag,j) in item.attribute.split(',')" closable
							:disable-transitions="false" @close="handleClose(tag,j,item)">
							{{tag}}
						</el-tag>
						<el-input class="input-new-tag" v-model="inputValues[index]" ref="saveTagInput" size="small"
							style="width:120px;" placeholder="请输入属性名"
							@keyup.enter.native="handleInputConfirm(index,item)">
						</el-input>
						<el-button class="button-new-tag" size="small" @click="handleInputConfirm(index,item)">添加
						</el-button>
					</div>
				</div>
				<div class="shop-amend-page__rule-actions" v-if="ruleshow">
					<div v-if="isshow" class="btn_specif">
						<el-button type="primary" @click="btnToclick()">添加新规格</el-button>
						<el-button type="success" @click="create()">立即生成</el-button>
					</div>
				</div>
				<div class="shop-amend-page__rule-form">
					<div v-if="!isshow" class="shop-amend-page__rule-form-inner">
						<div class="shop-amend-page__inline-field">
							<span class="shop-amend-page__inline-label">规格：</span>
							<el-input style="width:50%;" v-model="releobject.value" placeholder="例:颜色"></el-input>
						</div>
						<div class="shop-amend-page__inline-field">
							<span class="shop-amend-page__inline-label">规格值：</span>
							<el-input style="width:50%;" v-model="releobject.detail" placeholder="例:黑色"></el-input>
						</div>
						<div class="shop-amend-page__inline-field shop-amend-page__inline-field--actions">
							<el-button type="primary" @click="speciTo()">确 定</el-button>
							<el-button @click="speciTotwo()">取 消</el-button>
						</div>
					</div>
				</div>
			</div>
		</div>

		<div class="nf-table-card shop-amend-page__attr-card" v-if="valueshow">
			<div class="nf-filter-card__head">
				<i class="el-icon-goods nf-filter-card__head-icon"></i>
				<span>商品属性</span>
			</div>
			<div class="shop-amend-page__attr-wrap">
				<div class="property_table">
					<table style="width: 100%;">
						<tr style="display:flex;text-align: left;">
							<th style="width:120px;" v-for="(item,index) in headerData" :key="index">{{item}}</th>
						</tr>
					</table>
					<el-table :data="valueData">
						<el-table-column v-if="detailJson.length == 1 || detailJson.length == 0" prop="value[0]"
							width="120">
						</el-table-column>
						<el-table-column v-if="detailJson.length == 2" prop="value[0]" width="120">
						</el-table-column>
						<el-table-column v-if="detailJson.length == 2" prop="value[1]" width="120">
						</el-table-column>
						<el-table-column v-if="detailJson.length == 3" prop="value[0]" width="120">
						</el-table-column>
						<el-table-column v-if="detailJson.length == 3" prop="value[1]" width="120">
						</el-table-column>
						<el-table-column v-if="detailJson.length == 3" prop="value[2]" width="120">
						</el-table-column>
						<el-table-column v-if="detailJson.length == 4" prop="value[0]" width="120">
						</el-table-column>
						<el-table-column v-if="detailJson.length == 4" prop="value[1]" width="120">
						</el-table-column>
						<el-table-column v-if="detailJson.length == 4" prop="value[2]" width="120">
						</el-table-column>
						<el-table-column v-if="detailJson.length == 4" prop="value[3]" width="120">
						</el-table-column>
						<el-table-column v-if="detailJson.length == 5" prop="value[0]" width="120">
						</el-table-column>
						<el-table-column v-if="detailJson.length == 5" prop="value[1]" width="120">
						</el-table-column>
						<el-table-column v-if="detailJson.length == 5" prop="value[2]" width="120">
						</el-table-column>
						<el-table-column v-if="detailJson.length == 5" prop="value[3]" width="120">
						</el-table-column>
						<el-table-column v-if="detailJson.length == 5" prop="value[4]" width="120">
						</el-table-column>
						<el-table-column width="100">
							<template slot-scope="scope">
								<div class="imgWrap"
									style=" width:60px;height:60px;background-color: #fbfdff; border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 60px;">
									<el-upload class="avatar-uploader" v-model="scope.row.skuImg"
										:action="$http.adornUrlUp('alioss/upload')"
										:show-file-list="false" :on-success="handleAvatarSuccess">
										<img v-if="scope.row.skuImg" :src="scope.row.skuImg" class="avatar"
											style="border-radius: 6px;width:50px;height: 50px;" />
										<i v-else class="el-icon-plus avatar-uploader-icon"
											@click="curRowIndex=scope.$index"></i>
									</el-upload>
								</div>
							</template>
						</el-table-column>
						<el-table-column prop="skuOriginalPrice" width="120">
							<template slot-scope="scope">
								<div>
									<el-input type="text" v-model="scope.row.skuOriginalPrice"
										style="width:80px;font-size:14px;">
									</el-input>
								</div>
							</template>
						</el-table-column>
						<el-table-column prop="skuPrice" width="120">
							<template slot-scope="scope">
								<div>
									<el-input type="text" v-model="scope.row.skuPrice"
										style="width:80px;font-size:14px;">
									</el-input>
								</div>
							</template>
						</el-table-column>
						<el-table-column prop="memberPrice" width="120">
							<template slot-scope="scope">
								<div>
									<el-input type="text" v-model="scope.row.memberPrice"
										style="width:80px;font-size:14px;">
									</el-input>
								</div>
							</template>
						</el-table-column>
						<el-table-column prop="stock" width="120">
							<template slot-scope="scope">
								<div>
									<el-input type="text" v-model="scope.row.stock" style="width:80px;font-size:14px;">
									</el-input>
								</div>
							</template>
						</el-table-column>
						<el-table-column prop="sales" width="120">
							<template slot-scope="scope">
								<div>
									<el-input type="text" v-model="scope.row.sales" style="width:80px;font-size:14px;">
									</el-input>
								</div>
							</template>
						</el-table-column>
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

		<div class="nf-table-card shop-amend-page__attr-card" v-if="valueshow1">
			<div class="nf-filter-card__head">
				<i class="el-icon-goods nf-filter-card__head-icon"></i>
				<span>商品属性</span>
			</div>
			<div class="shop-amend-page__attr-wrap">
				<div class="property_table" style="display: inline-block;">
					<table style="width: 100%;">
						<tr style="display:flex;text-align: left;">
							<th style="width:150px;" v-for="(item,index) in headerData2" :key="index">{{item}}</th>
						</tr>
					</table>
					<el-table :data="sku">
						<el-table-column width="130">
							<template slot-scope="scope">
								<div class="imgWrap"
									style=" width:60px;height:60px;background-color: #fbfdff; border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 60px;">
									<el-upload class="avatar-uploader" v-model="scope.row.skuImg"
										:action="$http.adornUrlUp('alioss/upload')"
										:show-file-list="false" :on-success="handleAvatarSuccess3">
										<img v-if="scope.row.skuImg" :src="scope.row.skuImg" class="avatar"
											style="border-radius: 6px;width:50px;height: 50px;" />
										<i v-else class="el-icon-plus avatar-uploader-icon"
											@click="curRowIndex=scope.$index"></i>
									</el-upload>
								</div>
							</template>
						</el-table-column>
						<el-table-column prop="skuOriginalPrice" width="150">
							<template slot-scope="scope">
								<div>
									<el-input type="text" v-model="scope.row.skuOriginalPrice"
										style="width:80px;font-size:14px;">
									</el-input>
								</div>
							</template>
						</el-table-column>
						<el-table-column prop="skuPrice" width="150">
							<template slot-scope="scope">
								<div>
									<el-input type="text" v-model="scope.row.skuPrice"
										style="width:80px;font-size:14px;">
									</el-input>
								</div>
							</template>
						</el-table-column>
						<el-table-column prop="memberPrice" width="150">
							<template slot-scope="scope">
								<div>
									<el-input type="text" v-model="scope.row.memberPrice"
										style="width:80px;font-size:14px;">
									</el-input>
								</div>
							</template>
						</el-table-column>
						<el-table-column prop="stock" width="150">
							<template slot-scope="scope">
								<div>
									<el-input type="text" v-model="scope.row.stock" style="width:80px;font-size:14px;">
									</el-input>
								</div>
							</template>
						</el-table-column>
						<el-table-column prop="sales" width="150">
							<template slot-scope="scope">
								<div>
									<el-input type="text" v-model="scope.row.sales" style="width:80px;font-size:14px;">
									</el-input>
								</div>
							</template>
						</el-table-column>
					</el-table>
				</div>
			</div>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">
				<i class="el-icon-truck nf-filter-card__head-icon"></i>
				<span>物流设置</span>
			</div>
			<div class="shop-amend-page__shipping-row">
				<span class="shop-amend-page__field-label">是否包邮：</span>
				<el-radio-group v-model="positage" @change="agreeChange">
					<el-radio :label="1">是</el-radio>
					<el-radio :label="0">否</el-radio>
					<el-input v-if="priceshow" style="width:200px;margin-left: 10px;" placeholder="请输入邮费" type="number"
						v-model="postagePrice" autosize></el-input>
				</el-radio-group>
			</div>
		</div>

		<div class="nf-table-card shop-amend-page__detail-card">
			<div class="nf-filter-card__head">
				<i class="el-icon-document nf-filter-card__head-icon"></i>
				<span>商品详情</span>
			</div>
			<div class="shop-amend-page__editor-wrap">
				<quill-editor ref="myTextEditor" v-model="descrition" :options="quillOptions" class="shop-amend-page__editor">
				</quill-editor>
			</div>
		</div>

		<div class="nf-filter-card shop-amend-page__actions">
			<el-button type="primary" icon="el-icon-back" @click="prev">返回上一页</el-button>
			<el-button type="primary" icon="el-icon-check" @click="artiReleass">保存</el-button>
		</div>
	</div>
</template>

<script>
	import {
		quillEditor
	} from "vue-quill-editor";
	import "quill/dist/quill.core.css";
	import "quill/dist/quill.snow.css";
	import "quill/dist/quill.bubble.css";
	import quillConfig from "./quill-config.js";

	export default {
		name: 'Tinymce',
		components: {
			quillEditor,
		},
		data() {
			return {
				type: '',
				typeId: '',
				title: '',
				merchants: '',
				originalPrice: '',
				price: '',
				memberPrice: '',
				coverImg: '',
				guige: '',
				commissionPrice: '',
				integral: '',
				postagePrice: '',
				img: '',
				buyReason: '',
				sales: '',
				status: '',
				typeDatas: [],
				detailJson: [],
				imgs: [],
				value: [],
				potost: '',
				attr: [],
				brandId: '',
				brandIds: [],
				statusmain2: [{
						value: 1,
						label: '上架'
					},
					{
						value: 2,
						label: '下架'
					}
				],
				isExpress: '',
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
				positage: 1,
				valueshow: true,
				valueshow1: true,
				priceshow: false,
				specifdata: [],
				ruleValue: [],
				headerData: [],
				valueData: [],
				sku: [],
				releobject: {
					value: '',
					detail: ''
				},
				isshow: true,
				ruleshow: false,
				inputValues: {
					inputValue0: '',
					inputValue1: '',
					inputValue2: '',
					inputValue3: '',
					inputValue4: '',
					inputValue5: ''
				},
				ruleId: '',
				specif: '',
				propshow: false,
				guigeshow: true,
				attrName: '',
				headerData2: [],
				attrValue: [],
				skuImg: '',
				isSelect: '',
				homeGoods: '',
				isRecommend: '',
				isShow: false,
				hideUpload: false,
				limit: 1,
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
				descrition: '',
				curRowIndex: null,
				quillOptions: quillConfig,
				themeList: [],
				themeId: [],
				chandi:'',
				nav_id:'',
				navList:[],
			}
		},
		// watch: {
		//     descrition(val) {
		//         if (!this.hasChange && this.hasInit) {
		//             this.$nextTick(() => window.tinymce.get(this.tinymceId).setContent(val || ''))
		//         }
		//     }
		// },
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
				if (val == 0) {
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
					this.valueshow1 = true
					this.valueshow = false
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
				if (this.originalPrice == '') {
					this.originalPrice = '0'
				}
				if (this.price == '') {
					this.price = '0'
				}
				if (this.memberPrice == '') {
					this.memberPrice = '0'
				}
				this.$http({
					url: this.$http.adornUrl2('/admin/goods/danguige'),
					method: 'post',
					params: this.$http.adornParams({
						'originalPrice': this.originalPrice,
						'price': this.price,
						'memberPrice': this.memberPrice,
						'coverImg': this.coverImg,
					})
				}).then(({
					data
				}) => {
					let returnData = data.data;
					this.sku = returnData.value
					this.headerData2 = returnData.header
					this.valueshow = false
					this.propshow = true
					this.guigeshow = false
				})
			},
			// 回显规格
			echo(goodsId) {
				this.$http({
					url: this.$http.adornUrl2('/goods/formatAttr'),
					method: 'get',
					params: this.$http.adornParams({
						'goodsId': goodsId,
					})
				}).then(({
					data
				}) => {
					let returnData = data.data;
					this.sku = returnData.value
					this.headerData2 = returnData.header
					this.valueshow = false
					this.propshow = true
					this.guigeshow = false
				})
			},
			handleChange1(value) {
				this.value.forEach(element => {
					this.typeId = element;
				});
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
			},
			handleAvatarSuccess1(file, fileList) {
				this.coverImg = file.data
			},
			//上传成功
			handleUploadSuccess(file, fileList) {
				console.log(file);
				this.potost += file.data + ','
			},
			init() {
				// load(tinymceCDN, (err) => {
				//     if (err) {
				//         this.$message.error(err.message)
				//         return
				//     }
				//     this.initTinymce()
				// })
				let id = this.$route.query.id
        let store_id = this.$route.query.storeId
				this.$http({
					url: this.$http.adornUrl2('/admin/storegoods/store_goods_info'),
					method: 'post',
					params: this.$http.adornParams({
            'store_id':store_id,
						'goods_id': id
					})
				}).then(({
					data
				}) => {
					let returnData = data.data;
					let imgs = returnData.goods_images.split(',')
					this.title = returnData.title
					this.id = returnData.id;
					this.imgs = imgs;
					this.buyReason = returnData.buy_reason
					this.coverImg = returnData.goods_image

					this.commissionPrice = returnData.com_price
					this.integral = returnData.goods_jifen
					this.originalPrice = returnData.old_price;
					this.typeId = returnData.cate_id;
					this.price = returnData.new_price;
					this.sales = returnData.goods_sales;
					this.memberPrice = returnData.vip_price;

					this.descrition = returnData.goods_info;
					this.status = returnData.status;
					this.createAt = returnData.createAt;
					this.isSelect = returnData.is_jingxuan;
					this.homeGoods = returnData.is_index;
					this.isRecommend = returnData.is_tuijian;
					this.isExpress = returnData.is_fahuo;
					this.brandId = returnData.pinpai_id;
					let label = JSON.parse(returnData.type.up_id)
					let value = JSON.parse(returnData.type.id)
					this.value = [label, value]
					this.postagePrice = returnData.express_price
					if (returnData.theme_id) {
						// this.themeId = returnData.theme_id.split(',')
						let arr = returnData.theme_id.split(',')
						arr.forEach(res => {
							res = Number(res)
							this.themeId.push(res)
						})
					}
					if (returnData.is_baoyou == 0) {
						this.positage = 0
						this.priceshow = true
					} else {
						this.positage = 1
						this.priceshow = false
					}
					this.chandi = returnData.chandi
					this.nav_id = returnData.nav_id
					this.attr = returnData.attr

					if (this.attr.length != 0) {
						this.guige = 1
						this.attrName = returnData.attr.attr_name
						this.attrValue = returnData.attr.attrValue
						this.ruleId = returnData.attr.rule_id
						this.goodsId = returnData.attr.goods_id
						this.id = returnData.attr.id
						this.Triggerselect(returnData.id)
						this.create2(returnData.id)

					} else {
						this.guige = 0
						this.dansku(this.id)
						this.ruleId = 0
						this.sku = returnData.sku
					}
				})
			},
			// 修改商品
			artiReleass() {
				let id = this.$route.query.id
				if (this.imgs == '') {
					this.img = this.potost
					this.img = this.img.substr(0, this.img.length - 1)
				} else {
					this.img = this.potost + this.imgs
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
				if (this.postagePrice === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入邮费',
						type: 'warning'
					});
					return
				}
				this.$http({
					url: this.$http.adornUrl2('/admin/goods/edit_goods'),
					method: 'post',
					data: this.$http.adornData({
						'goods_id': id,
						'goods_images': this.img,
						'title': this.title,
						'cate_id': this.typeId,
						'old_price': this.originalPrice,
						'new_price': this.price,
						'vip_price': this.memberPrice,
						'goods_sales': this.sales,
						'pinpai_id': this.brandId,
						'status': this.status,
						'goods_image': this.coverImg,
						'descrition': this.descrition,
						'com_price': this.commissionPrice,
						'goods_jifen': this.integral,
						'is_jingxuan': this.isSelect,
						'is_index': this.homeGoods,
						'buy_reason': this.buyReason,
						'is_tuijian': this.isRecommend,
						'is_fahuo': this.isExpress,
						'express_price': this.postagePrice,
						'sku': this.sku,
						'attr': this.attr,
						'createAt': this.createAt,
						'is_baoyou': this.positage,
						'is_guige': this.guige,
						'guige_id': this.ruleId,
						'theme_id': this.themeId.join(','),
						'chandi':this.chandi,
						'nav_id':this.nav_id,
					})
				}).then(({
					data
				}) => {
					this.$message({
						message: '操作成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.$router.push({
								path: '/shopAdmin'
							})
						}
					})
				})
			},
			// initTinymce() {
			//     const _this = this
			//     window.tinymce.init({
			//         selector: `#${this.tinymceId}`,
			//         images_upload_handler: function (blobInfo, success, failure) {
			//             var xhr, formData;

			//             xhr = new XMLHttpRequest();
			//             xhr.withCredentials = false;
			//             xhr.open('POST', this.$http.adornUrlUp('alioss/upload'));
			//             xhr.onload = function () {
			//                 var json;

			//                 if (xhr.status != 200) {
			//                     failure('HTTP Error: ' + xhr.status);
			//                     return;
			//                 }
			//                 json =JSON.parse(xhr.responseText);
			//                 if (json == "" || json == null) {
			//                     failure('Invalid JSON: ' + xhr.responseText);
			//                     return;
			//                 }
			//                 success(json.data);
			//             };
			//             formData = new FormData();
			//             formData.append('file', blobInfo.blob(), blobInfo.filename());
			//             xhr.send(formData);
			//         },
			//         language: this.languageTypeList['zh'],
			//         height: this.height,
			//         body_class: 'panel-body ',
			//         object_resizing: false,
			//         toolbar: this.toolbar.length > 0 ? this.toolbar : toolbar,
			//         plugins,
			//         menubar: this.menubar,
			//         end_container_on_empty_block: true,
			//         powerpaste_word_import: 'clean',
			//         code_dialog_height: 450,
			//         code_dialog_width: 1000,
			//         advlist_bullet_styles: 'square',
			//         advlist_number_styles: 'default',
			//         imagetools_cors_hosts: ['www.tinymce.com', 'codepen.io'],
			//         default_link_target: '_blank',
			//         link_title: false,
			//         // Image
			//         imagetools_toolbar: 'rotateleft rotateright | flipv fliph | editimage imageoptions',
			//         nonbreaking_force_tab: true, // inserting nonbreaking space &nbsp; need Nonbreaking Space Plugin
			//         init_instance_callback: editor => {
			//             if (_this.descrition) {
			//                 editor.setContent(_this.descrition)
			//             }
			//             _this.hasInit = true
			//             editor.on('NodeChange Change KeyUp SetContent', () => {
			//                 this.hasChange = true
			//                 this.descrition = editor.getContent()
			//             })
			//         },
			//         setup(editor) {
			//             editor.on('FullscreenStateChanged', (e) => {
			//                 _this.fullscreen = e.state
			//             })
			//         }
			//     })
			// },
			// destroyTinymce() {
			//     const tinymce = window.tinymce.get(this.tinymceId)
			//     if (this.fullscreen) {
			//         tinymce.execCommand('mceFullScreen')
			//     }
			//     if (tinymce) {
			//         tinymce.destroy()
			//     }
			// },
			// 商品规格
			specifSelect() {
				this.$http({
					url: this.$http.adornUrl2('/admin/goods/guige_list'),
					method: 'post',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					let returnData = data.data;
					this.specifdata = returnData;
				})
			},
			// 商品规格
			Triggerselect(id) {
				this.$http({
					url: this.$http.adornUrl2('/admin/goods/edit_duoguige_value'),
					method: 'post',
					params: this.$http.adornParams({
						'goodsId': id
					})
				}).then(({
					data
				}) => {
					let returnData = data.data
					returnData.attrValue.forEach(res => {
						res.attribute = res.detail
						res.rule_value = res.value
					})
					this.attr = returnData
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
					url: this.$http.adornUrl2('/admin/goods/guige_list'),
					method: 'post',
					params: this.$http.adornParams({
						'rule_id': val
					})
				}).then(({
					data
				}) => {
					let returnData = data.data;
					this.ruleValue = returnData.rule_value
					this.attrName = returnData.rule_name
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
					rule_value: this.releobject.value,
					attribute: this.releobject.detail
				});
				this.isshow = true
				this.releobject.value = ''
				this.releobject.detail = ''
			},
			handleClose(tag, j, item) {
				var detailarr = item.attribute.split(',')
				detailarr.splice(detailarr.indexOf('tag'), 1);
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
					item.attribute = String(detailarr)
				}
			},
			handleInputConfirm(index, item) {
				let inputValue = this.inputValues[index];
				if (inputValue) {
					var detailarr = item.attribute.split(',')
					detailarr.push(inputValue)
					for (var i = 0; i < detailarr.length; i++) {
						for (var j = 0; j < detailarr.length; j++) {
							if (detailarr[i] == detailarr[j] && i != j) {
								detailarr.splice(j, 1);
							}
						}
					}
					item.attribute = String(detailarr)
					this.inputValues[index] = '';
				}
				this.inputVisible = false;
			},
			// 初始化立即生成
			create2(id) {
				this.$http({
					url: this.$http.adornUrl2(`/admin/goods/edit_duoguige_attr`),
					method: 'post',
					params: this.$http.adornParams({
						goodsId: id
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
				this.attr = {}
				this.attr = {
					attrName: this.attrName,
					attrValue: this.ruleValue,
					ruleId: this.ruleId
				}
				if (this.originalPrice == '') {
					this.originalPrice = '0'
				}
				if (this.price == '') {
					this.price = '0'
				}
				if (this.memberPrice == '') {
					this.memberPrice = '0'
				}
				this.$http({
					url: this.$http.adornUrl2(
						`/admin/goods/duoguige?coverImg=${this.coverImg}&originalPrice=${this.originalPrice}&price=${this.price}&memberPrice=${this.memberPrice}`
						),
					method: 'post',
					data: this.attr
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
					url: this.$http.adornUrl2('/admin/goods/goods_brand'),
					method: 'post',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.brandIds = returnData
				})
			},
			//获取主题
			selectThemeList() {
				this.tableDataLoading = true;
				this.$http({
					url: this.$http.adornUrl2("/admin/goods/goods_theme"),
					method: "post",
					params: this.$http.adornParams({}),
				}).then(({
					data
				}) => {
					this.tableDataLoading = false;
					let returnData = data.data;
					this.themeList = returnData;
				});
			},
			// 商品分类
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl2('/admin/goods/goods_cate'),
					method: 'post',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.typeDatas = returnData
				})
			},
			// 获取导航归属
			navSelect(){
				this.$http({
					url: this.$http.adornUrl2("/admin/goods/nav_list"),
					method: "post",
					params: this.$http.adornParams({}),
				}).then(({
					data
				}) => {
					let returnData = data.data;
					this.navList = returnData;
				});
			},
		},
		mounted() {
			this.init()
			this.dataSelect()
			this.specifSelect()
			this.brandSelect()
			this.selectThemeList()
			this.navSelect()
		},
		// beforeDestroy() {
		//     this.destroyTinymce()
		// },
		// activated() {
		//     if (window.tinymce) {
		//         this.initTinymce()
		//     }
		// 	this.init()
		// 	this.dataSelect()
		// 	this.specifSelect()
		// 	this.brandSelect()
		// },
		// deactivated() {
		//     this.destroyTinymce()
		// }
	}
</script>

<style scoped>
.shop-amend-page {
	overflow-x: hidden;
	padding-bottom: 16px;
}

.shop-amend-page__back {
	margin-bottom: 12px;
}

.shop-amend-page__back-link {
	display: inline-flex;
	align-items: center;
	font-size: 14px;
	color: #606266;
	text-decoration: none;
}

.shop-amend-page__back-link:hover {
	color: #5cc477;
}

.shop-amend-page__back-icon {
	width: 1.2em;
	height: 1.2em;
	margin-right: 4px;
}

.shop-amend-page__form-body .el-col {
	margin-bottom: 4px;
	font-size: 14px;
	color: #606266;
}

.shop-amend-page__field-label {
	flex: 0 0 auto;
	min-width: 100px;
	font-size: 14px;
	color: #606266;
}

.shop-amend-page__upload-row {
	display: flex;
	align-items: center;
	margin-bottom: 16px;
	gap: 12px;
}

.shop-amend-page__upload-row--wrap {
	flex-wrap: wrap;
	align-items: flex-start;
}

.shop-amend-page__upload-box {
	background-color: #fbfdff;
	border: 1px dashed #c0ccda;
	border-radius: 6px;
	text-align: center;
}

.shop-amend-page__upload-box--cover {
	width: 148px;
	height: 148px;
	line-height: 148px;
}

.shop-amend-page__carousel-item {
	position: relative;
	margin: 5px;
}

.shop-amend-page__spec-row {
	display: flex;
	align-items: center;
	flex-wrap: wrap;
	gap: 12px;
}

.shop-amend-page__spec-select {
	margin-left: 8px;
}

.shop-amend-page__rule-block {
	margin-top: 12px;
	padding-top: 12px;
	border-top: 1px solid #e8eaed;
}

.shop-amend-page__rule-actions {
	margin-top: 16px;
}

.shop-amend-page__rule-actions .el-button + .el-button {
	margin-left: 12px;
}

.shop-amend-page__rule-form-inner {
	display: flex;
	flex-wrap: wrap;
	align-items: center;
	gap: 12px;
	margin-top: 16px;
}

.shop-amend-page__inline-field {
	display: inline-flex;
	align-items: center;
	gap: 8px;
}

.shop-amend-page__inline-label {
	width: 80px;
	text-align: right;
	font-size: 14px;
	color: #606266;
}

.shop-amend-page__inline-field--actions .el-button + .el-button {
	margin-left: 8px;
}

.shop-amend-page__attr-wrap {
	overflow-x: auto;
}

.shop-amend-page__shipping-row {
	display: flex;
	align-items: center;
	flex-wrap: wrap;
	gap: 12px;
	min-height: 38px;
}

.shop-amend-page__editor-wrap {
	width: 100%;
}

.shop-amend-page__editor {
	padding-bottom: 50px;
	height: 300px;
	width: 100%;
	display: inline-table;
	margin-bottom: 16px;
}

.shop-amend-page__actions {
	display: flex;
	justify-content: center;
	align-items: center;
	gap: 16px;
	margin-bottom: 0;
}

.shop-amend-page__actions .el-button + .el-button {
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
</style>
