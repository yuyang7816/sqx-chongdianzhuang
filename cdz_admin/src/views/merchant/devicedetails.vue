<template>
	<div class="nf-page devicedetails-page">
		<a class="devicedetails-page__back" href="#" @click.prevent="prev">
			<icon-svg name="jiantou" class="devicedetails-page__back-icon"></icon-svg>
			返回
		</a>

		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">设备管理</span>
			<h2 class="nf-page-hero__title">设备列表</h2>
			<p class="nf-page-hero__desc">查看与管理台球馆桌台绑定设备，支持按桌馆类型、桌台名称、桌台状态筛选，可查询、重置、刷新及设备绑定操作。</p>
			<div class="nf-page-hero__bar"></div>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">
				<i class="el-icon-search nf-filter-card__head-icon"></i>
				<span>筛选条件</span>
			</div>
			<el-form :inline="true" class="nf-filter-card__body" @submit.native.prevent>
				<el-form-item label="桌馆类型">
					<el-select
						class="devicedetails-page__filter-select"
						v-model="goodsCategoryT"
						placeholder="请选择桌馆类型"
						@change="select()">
						<el-option v-for="item in goodsCategorymain" :key="item.value" :label="item.label"
							:value="item.value">
						</el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="桌台名称">
					<el-input
						class="devicedetails-page__filter-input"
						@keydown.enter.native="select"
						clearable
						placeholder="请输入桌台名称"
						v-model="title">
					</el-input>
				</el-form-item>
				<el-form-item label="桌台状态">
					<el-select
						class="devicedetails-page__filter-select"
						v-model="statusT"
						placeholder="请选择桌台状态"
						@change="select()">
						<el-option v-for="item in statusmain" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</el-form-item>
				<el-form-item class="devicedetails-page__filter-actions">
					<el-button type="primary" icon="el-icon-search" @click="select">查询</el-button>
					<el-button type="primary" icon="el-icon-refresh-left" @click="cleans">重置</el-button>
					<el-button type="primary" icon="el-icon-refresh" @click="refresh">刷新</el-button>
				</el-form-item>
			</el-form>
		</div>

		<el-tabs v-model="activeName" class="devicedetails-page__tabs">
			<el-tab-pane label="设备列表" name="shop">
				<div class="nf-table-card">
			<el-table v-loading="tableDataLoading" :data="shopData.list">
				<el-table-column fixed prop="goodsId" label="编号" width="80">
				</el-table-column>
				<el-table-column fixed prop="goodsCoverImage" label="桌台图片">
					<template slot-scope="scope">
						<img :src="scope.row.goodsCoverImage" alt="" width="60" height="60">
					</template>
				</el-table-column>
				<el-table-column prop="goodsCategory" label="桌馆类型" width="80">
					<template slot-scope="scope">
						<span class="devicedetails-page__type-billiard" v-if="scope.row.goodsCategory  == 1">台球桌</span>
						<span class="devicedetails-page__type-chess" v-if="scope.row.goodsCategory  == 2">棋牌桌</span>
						<span class="devicedetails-page__type-other" v-if="scope.row.goodsCategory  == 3">其他桌</span>
						<span class="devicedetails-page__type-cabinet" v-if="scope.row.goodsCategory  == 4">柜子</span>
					</template>
				</el-table-column>
				<el-table-column prop="goodsName" label="桌台名称" width="200">
				</el-table-column>
				<el-table-column prop="counts" label="绑定设备数量" width="80">
				</el-table-column>
				<el-table-column prop="goodsType" label="桌台信息" width="200">
				</el-table-column>
				<el-table-column prop="hourPrice" label="小时价">
					<template slot-scope="scope">
						<span class="devicedetails-page__price">{{scope.row.hourPrice?scope.row.hourPrice:0 | numFilter}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="minutePrice" label="分钟价">
					<template slot-scope="scope">
						<span class="devicedetails-page__price">{{scope.row.minutePrice?scope.row.minutePrice:0 | numFilter}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="cashPledge" label="押金">
					<template slot-scope="scope">
						<span class="devicedetails-page__price">{{scope.row.cashPledge?scope.row.cashPledge:0 | numFilter}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="subscription" label="预约价格">
					<template slot-scope="scope">
						<span class="devicedetails-page__price">{{scope.row.subscription?scope.row.subscription:0 | numFilter}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="yyToken" label="语音播报token" width="200">
				</el-table-column>
				<el-table-column prop="yyId" label="语音播报ID" width="200">
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="160">
				</el-table-column>
				<el-table-column fixed='right' label="上下架">
					<template slot-scope="scope">
						<el-switch v-model="scope.row.status" @change="change(scope.row.id,scope.row.status)"
							:active-value="openValue" :inactive-value="closeValue" active-color="#5cc477"
							inactive-color="#ff4949" v-if="scope.row.status == 1|| scope.row.status == 2">
						</el-switch>
						<span class="devicedetails-page__status-clean" v-else-if="scope.row.status == 3 ">清洁中</span>
						<span class="devicedetails-page__status-maintain" v-else-if="scope.row.status == 4 ">维护中</span>
						<span class="devicedetails-page__status-closed" v-else-if="scope.row.status == 5 ">已打烊</span>
					</template>
				</el-table-column>
				<el-table-column fixed="right" label="操作" width="120" style="line-height: 33px;">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" @click="updatesshebei(scope.row)" style="margin: 5px;" v-if="scope.row.goodsCategory != 4">修改设备
						</el-button>
						<el-button size="mini" type="primary" plain @click="switchkg(scope.row,1)" style="margin: 5px;" v-if="scope.row.goodsCategory != 4"
							:disabled="!isAuth('merchantList:update')">设备详情</el-button>
						<el-button size="mini" type="primary" plain @click="switchkg(scope.row,2)" style="margin: 5px;" v-if="scope.row.goodsCategory == 4"
							:disabled="!isAuth('merchantList:update')">设备详情</el-button>
					</template>
				</el-table-column>
			</el-table>
					<div class="devicedetails-page__pagination">
						<el-pagination @size-change="handleSizeChange1" @current-change="handleCurrentChange1"
							:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
							layout="total,sizes, prev, pager, next" :total="shopData.totalCount">
						</el-pagination>
					</div>
				</div>
			</el-tab-pane>
		</el-tabs>

			<el-dialog title="设备清单" :visible.sync="dialogFormVisible10" center width="80%">
				<!-- 开关列表 -->
				<div>
					<div style="margin-bottom: 10px;">
						<el-button type="primary" size="small" @click="shauxinBtn()" plain>刷新</el-button>
						<el-button type="primary" size="small" @click="addshebei()"
							:disabled="!isAuth('devices:add')">添加</el-button>
					</div>
					<el-table :data="tableData">
						<el-table-column prop="roomId" label="编号" width="150">
						</el-table-column>
						<el-table-column prop="roomType" label="设备类型">
							<template slot-scope="scope">
								<span v-if="scope.row.roomType==2">插座</span>
								<span v-if="scope.row.roomType==1">设备</span>
								<span v-if="scope.row.roomType==6">锁球器</span>
								<span v-if="scope.row.roomType==7">四路设备</span>
								<span v-if="scope.row.roomType==8">门禁锁</span>
								<span v-if="scope.row.roomType==9">四路语音</span>
							</template>
						</el-table-column>
						<el-table-column prop="roomName" label="设备名称/智能插座名称">
						</el-table-column>
						<el-table-column prop="roomNo" label="设备编号/智能插座mac码">
						</el-table-column>
						<!-- 
						<el-table-column prop="roomName" label="智能插座名称" v-if="goodsCategory==2">
						</el-table-column>
						<el-table-column prop="roomNo" label="智能插座mac码" v-if="goodsCategory==2">
						</el-table-column> -->
						<el-table-column label="操作" width="160">
							<template slot-scope="scope">
								<el-button type="primary" size="small" @click="handcilckxiugai(scope.row)"
									:disabled="!isAuth('devices:update')">修改</el-button>
								<el-button type="warning" size="small" @click="handcilckDel(scope.row)"
									:disabled="!isAuth('devices:update')">删除</el-button>
							</template>
						</el-table-column>
					</el-table>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible10 = false">取 消</el-button>
					<el-button type="primary" @click="switchamendNoticeTo()">确 定</el-button>
				</div>
			</el-dialog>
			<el-dialog title="设备清单" :visible.sync="dialogFormVisible1" center width="80%">
				<div>
					<div style="margin-bottom: 10px;">
						<el-button type="primary" size="small" @click="shauxinBtn()" plain>刷新</el-button>
						<el-button type="primary" size="small" @click="addshebei()"
							:disabled="!isAuth('devices:add')">添加</el-button>
					</div>
					<el-table :data="tableData">
						<el-table-column prop="roomId" label="编号" width="150">
						</el-table-column>
						<el-table-column prop="roomType" label="设备类型">
							<template slot-scope="scope">
								<span v-if="scope.row.roomType==3">闪开闪关</span>
								<span v-if="scope.row.roomType==4">语音</span>
								<span v-if="scope.row.roomType==5">称重</span>
								<span v-if="scope.row.roomType==6">锁球器</span>
								<span v-if="scope.row.roomType==7">四路设备</span>
								<span v-if="scope.row.roomType==8">门禁锁</span>
								<span v-if="scope.row.roomType==9">四路语音</span>
							</template>
						</el-table-column>
						<el-table-column prop="serveCode" label="服务编码">
						</el-table-column>
						<el-table-column prop="equipmentCode" label="设备模型编码">
						</el-table-column>
						<el-table-column prop="roomSelect" label="继电器序号">
						</el-table-column>
						<el-table-column prop="musicSelectStart" label="开启语音">
						</el-table-column>
						<el-table-column prop="musicSelectEnd" label="关闭语音">
						</el-table-column>
						<el-table-column prop="deviceId" label="设备ID">
						</el-table-column>
						<!-- <el-table-column prop="roomWeight" label="设备重量">
						</el-table-column> -->
						
						<el-table-column label="操作" width="160">
							<template slot-scope="scope">
								<el-button type="primary" size="small" @click="handcilckxiugai(scope.row)"
									:disabled="!isAuth('devices:update')">修改</el-button>
								<el-button type="warning" size="small" @click="handcilckDel(scope.row)"
									:disabled="!isAuth('devices:update')">删除</el-button>
							</template>
						</el-table-column>
					</el-table>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible1 = false">取 消</el-button>
					<el-button type="primary" @click="switchamendNoticeTo()">确 定</el-button>
				</div>
			</el-dialog>
			<el-dialog title="添加设备" :visible.sync="dialogFormVisible12" center>
				<el-form>
					<el-form-item label="设备类型：" :label-width="formLabelWidth">
						<el-radio-group v-model="roomType">
							<el-radio :label="1" v-if="goodsCategory!=4">设备</el-radio>
							<el-radio :label="2" v-if="goodsCategory!=4">插座</el-radio>
							<el-radio :label="3" v-if="goodsCategory==4">闪开闪关</el-radio>
							<el-radio :label="4" v-if="goodsCategory==4">语音</el-radio>
							<el-radio :label="5" v-if="goodsCategory==4">称重</el-radio>
							<el-radio :label="6">锁球器</el-radio>
							<el-radio :label="7">四路设备</el-radio>
							<el-radio :label="8">门禁锁</el-radio>
							<el-radio :label="9">四路语音</el-radio>
						</el-radio-group>
					</el-form-item>
					<el-form-item label="设备：" :label-width="formLabelWidth" v-if="roomType==1">
						<el-select v-model="roomNo" placeholder="请选择设备" style="width:250px;" @change="shebeiBtn()">
							<el-option v-for="item in bianhaoList" :key="item.vmsDeviceNo" :label="item.vmsDeviceNote"
								:value="item.vmsDeviceNo">
							</el-option>
						</el-select>
					</el-form-item>
					<el-form-item label="设备编号 ：" :label-width="formLabelWidth" v-if="roomType==6||roomType==7||roomType==8">
						<el-input style="width:250px;" v-model="roomNo" placeholder="请输入设备编号"></el-input>
					</el-form-item>
					<el-form-item label="智能插座mac码 ：" :label-width="formLabelWidth" v-if="roomType==2">
						<el-input style="width:250px;" v-model="roomNo" placeholder="请输入智能插座mac码"></el-input>
					</el-form-item>
					<el-form-item label="智能插座名称 ：" :label-width="formLabelWidth" v-if="roomType==2">
						<el-input style="width:250px;" v-model="roomName" placeholder="请输入智能插座名称"></el-input>
					</el-form-item>
					<el-form-item label="服务编码 ：" :label-width="formLabelWidth" v-if="goodsCategory==4">
						<el-input style="width:250px;" v-model="serveCode" placeholder="请输入服务编码"></el-input>
					</el-form-item>
					<el-form-item label="设备模型编码 ：" :label-width="formLabelWidth" v-if="goodsCategory==4">
						<el-input style="width:250px;" v-model="equipmentCode" placeholder="请输入设备模型编码"></el-input>
					</el-form-item>
					<el-form-item label="继电器序号 ：" :label-width="formLabelWidth" v-if="roomType==3||roomType==7">
						<el-input style="width:250px;" v-model="roomSelect" placeholder="请输入继电器序号"></el-input>
					</el-form-item>
					<el-form-item label="开启语音 ：" :label-width="formLabelWidth" v-if="roomType==4">
						<el-input style="width:250px;" v-model="musicSelectStart" placeholder="请输入开启语音"></el-input>
					</el-form-item>
					<el-form-item label="关闭语音 ：" :label-width="formLabelWidth" v-if="roomType==4">
						<el-input style="width:250px;" v-model="musicSelectEnd" placeholder="请输入关闭语音"></el-input>
					</el-form-item>
					<el-form-item label="设备ID ：" :label-width="formLabelWidth" v-if="roomType==5">
						<el-input style="width:250px;" v-model="deviceId" placeholder="请输入设备ID"></el-input>
					</el-form-item>
					<!-- <el-form-item label="设备重量 ：" :label-width="formLabelWidth" v-if="roomType==5">
						<el-input style="width:250px;" v-model="roomWeight" placeholder="请输入设备重量"></el-input>
					</el-form-item> -->
					
				</el-form>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible12= false">取 消</el-button>
					<el-button type="primary" @click="handcilckaddshebei()">确 定</el-button>
				</div>
			</el-dialog>
			<el-dialog title="修改设备" :visible.sync="dialogFormVisible11" center>
				<el-form :model="couponform">
					<el-form-item label="设备类型：" :label-width="formLabelWidth">
						<el-radio-group v-model="couponform.roomType">
							<el-radio :label="1" v-if="goodsCategory!=4">设备</el-radio>
							<el-radio :label="2" v-if="goodsCategory!=4">插座</el-radio>
							<el-radio :label="3" v-if="goodsCategory==4">闪开闪关</el-radio>
							<el-radio :label="4" v-if="goodsCategory==4">语音</el-radio>
							<el-radio :label="5" v-if="goodsCategory==4">称重</el-radio>
							<el-radio :label="6">锁球器</el-radio>
							<el-radio :label="7">四路设备</el-radio>
							<el-radio :label="8">门禁锁</el-radio>
							<el-radio :label="9">四路语音</el-radio>
						</el-radio-group>
					</el-form-item>
					<el-form-item label="设备：" :label-width="formLabelWidth" v-if="couponform.roomType==1">
						<el-select v-model="couponform.roomNo" placeholder="请选择设备" style="width:250px;"
							@change="shebeiBtn()">
							<el-option v-for="item in bianhaoList" :key="item.vmsDeviceNo" :label="item.vmsDeviceNote"
								:value="item.vmsDeviceNo">
							</el-option>
						</el-select>
					</el-form-item>
					<el-form-item label="设备编号 ：" :label-width="formLabelWidth" v-if="couponform.roomType==6||couponform.roomType==7||couponform.roomType==8">
						<el-input style="width:250px;" v-model="couponform.roomNo" placeholder="请输入设备编号"></el-input>
					</el-form-item>
					<el-form-item label="智能插座mac码 ：" :label-width="formLabelWidth" v-if="couponform.roomType==2">
						<el-input style="width:250px;" v-model="couponform.roomNo" placeholder="请输入智能插座mac码"></el-input>
					</el-form-item>
					<el-form-item label="智能插座名称 ：" :label-width="formLabelWidth" v-if="couponform.roomType==2">
						<el-input style="width:250px;" v-model="roomName" placeholder="请输入智能插座名称"></el-input>
					</el-form-item>
					<el-form-item label="服务编码 ：" :label-width="formLabelWidth" v-if="goodsCategory==4">
						<el-input style="width:250px;" v-model="couponform.serveCode" placeholder="请输入服务编码"></el-input>
					</el-form-item>
					<el-form-item label="设备模型编码 ：" :label-width="formLabelWidth" v-if="goodsCategory==4">
						<el-input style="width:250px;" v-model="couponform.equipmentCode" placeholder="请输入设备模型编码"></el-input>
					</el-form-item>
					<el-form-item label="继电器序号 ：" :label-width="formLabelWidth" v-if="couponform.roomType==3||couponform.roomType==7">
						<el-input style="width:250px;" v-model="couponform.roomSelect" placeholder="请输入继电器序号"></el-input>
					</el-form-item>
					<el-form-item label="开启语音 ：" :label-width="formLabelWidth" v-if="couponform.roomType==4">
						<el-input style="width:250px;" v-model="couponform.musicSelectStart" placeholder="请输入开启语音"></el-input>
					</el-form-item>
					<el-form-item label="关闭语音 ：" :label-width="formLabelWidth" v-if="couponform.roomType==4">
						<el-input style="width:250px;" v-model="couponform.musicSelectEnd" placeholder="请输入关闭语音"></el-input>
					</el-form-item>
					<el-form-item label="设备ID ：" :label-width="formLabelWidth" v-if="couponform.roomType==5">
						<el-input style="width:250px;" v-model="couponform.deviceId" placeholder="请输入设备ID"></el-input>
					</el-form-item>
					<!-- <el-form-item label="设备重量 ：" :label-width="formLabelWidth" v-if="couponform.roomType==5">
						<el-input style="width:250px;" v-model="couponform.roomWeight" placeholder="请输入设备重量"></el-input>
					</el-form-item> -->
					
				</el-form>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible11= false">取 消</el-button>
					<el-button type="primary" @click="handcilckxiugaishebei()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 设备 -->
			<el-dialog title="修改设备" :visible.sync="dialogFormVisibleshebie" center>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">设备：</span>
					<el-select v-model="innerCode" placeholder="请选择设备" style="width:250px;">
						<el-option v-for="item in bianhaoList1" :key="item.innerCode" :label="item.vmsGroupName"
							:value="item.innerCode">
						</el-option>
					</el-select>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisibleshebie = false">取 消</el-button>
					<el-button type="primary" @click="disposeNoticeToshiebei()">确 定</el-button>
				</div>
			</el-dialog>
	</div>
</template>
<script>
	export default {
		data() {
			return {
				type: 0,

				tableData: [],
				bianhao: '',
				roomType: '',
				roomName: '',
				roomNo:'',
				serveCode:'',
				equipmentCode:'',
				roomSelect:'',
				musicSelectStart:'',
				musicSelectEnd:'',
				deviceId:'',
				roomWeight:'',
				couponform: {
					bianhao: '',
					roomType: '',
					equipment_id: '',
					roomName: '',
					serveCode:'',
					equipmentCode:'',
					roomSelect:'',
					musicSelectStart:'',
					musicSelectEnd:'',
					deviceId:'',
					roomWeight:'',
					roomNo:'',
				},
				formLabelWidth: '200px',
				activeName: 'shop',
				size: 10,
				page: 1,
				id: '',
				shopData: {},

				classDatas: [],
				title: '',
				statusT: 0,
				statusmain: [{
						value: 0,
						label: '全部'
					},
					{
						value: 1,
						label: '上架'
					},
					{
						value: 2,
						label: '下架'
					}
				],
				formLabelWidth: '200px',
				tableDataLoading: false,
				dialogFormVisible10: false,
				dialogFormVisible11: false,
				dialogFormVisible12: false,
				openValue: 1,
				closeValue: 2,
				equipment_id: '',
				bianhaoList: [],

				equipment_sn: '',
				merchant_id: '',
				goods_id: '',
				store_id: '',
				dialogFormVisibleshebie: false,

				innerCode: '',
				bianhaoList1: [],
				goodsCategoryT: '',
				goodsCategorymain: [{
						value: '',
						label: '全部'
					},
					{
						value: 1,
						label: '台球桌'
					},
					{
						value: 2,
						label: '棋牌桌'
					},
					{
						value: 3,
						label: '其他桌'
					},
					{
						value: 4,
						label: '柜子'
					}
				],
				goodsCategory: '',
				dialogFormVisible1:false,
				
			}
		},
		filters: {
			numFilter(value) {
				let realVal = ''
				if (!isNaN(value) && value !== '') {
					// 截取当前数据到小数点后两位
					realVal = parseFloat(value).toFixed(2)
				} else {
					realVal = '--'
				}
				return realVal
			},
			//时间戳转换为YYYY-MM-DD HH:mm:ss
			formateDate(time) {
				var date = new Date(time * 1000); //时间戳为10位需*1000，时间戳为13位的话不需乘1000
				var Y = date.getFullYear() + '-';
				var M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
				var D = date.getDate() + ' ';
				var h = date.getHours() + ':';
				var m = date.getMinutes() + ':';
				var s = date.getSeconds();
				return Y + M + D + h + m + s;
			}
		},
		methods: {
			// 返回上一级
			prev() {
				this.$router.back()
			},
			goBack() {
				this.$router.back()
			},
			// 刷新（台球馆管理）
			refresh() {
				this.shopSelect()
			},
			handleSizeChange1(val) {
				this.size = val
				this.shopSelect()
			},
			handleCurrentChange1(val) {
				this.page = val
				this.shopSelect()
			},
			select() {
				this.shopSelect()
			},
			cleans() {
				this.title = ''
				this.statusT = 0
				this.typeId = ''
				this.goodsCategoryT = ''
				this.value = []
				this.shopSelect()
			},
			classSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl2('/admin/storegoods/goods_cate'),
					method: 'get',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					// returnData = JSON.parse(JSON.stringify(returnData).replace(/"id"/g, '"value"'))
					// returnData = JSON.parse(JSON.stringify(returnData).replace(/"name"/g, '"label"'))
					this.classDatas = returnData
					// console.log(this.classDatas)
				})
			},
			shopSelect() {
				let storeId = this.$route.query.storeId
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('goods/selectGoodsList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.size,
						'goodsName': this.title,
						'status': this.statusT,
						'tearoomId': storeId,
						'goodsCategory': this.goodsCategoryT,
					})

				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.shopData = returnData
					this.shebeidata = returnData.data
				})
			},
			change(id, type) {
				let types = type
				this.$http({
					url: this.$http.adornUrl2('/admin/goods/edit_goods_status'),
					method: 'get',
					params: this.$http.adornParams({
						'goods_id': id,
						'type': types
					})
				}).then(({
					data
				}) => {
					if (data && data.code == 0) {
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
						})
						this.shopSelect()
					}
				})
			},
			//开关管理
			switchkg(row,index) {
				console.log(row)
				this.goods_id = row.goodsId
				this.innerCode = row.innerCode
				this.goodsCategory = row.goodsCategory
				if(index==1){
					this.dialogFormVisible10 = true
				}else{
					this.dialogFormVisible1 = true
				}
				this.dataSelect()
				
				
			},
			// 刷新
			shauxinBtn(){
				this.dataSelect()
			},
			// 获取已绑定设备列表
			dataSelect() {
				let storeId = this.$route.query.storeId
				this.$http({
					url: this.$http.adornUrl('room/selectRoomList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': 1,
						'limit': 20,
						'roomName': '',
						'goodsId': this.goods_id,
					})
				}).then(({
					data
				}) => {
					let returnData = data.data;
					this.tableData = returnData.list
				})
			},
			switchamendNoticeTo() {
				this.dialogFormVisible10 = false
			},
			// 获取设备列表
			shebeiSelect() {
				let storeId = this.$route.query.storeId
				this.$http({
					url: this.$http.adornUrl('room/selectGroup'),
					method: 'get',
					params: this.$http.adornParams({
						'innerCode': this.innerCode,
					})
				}).then(({
					data
				}) => {
					let returnData = data.data.result;
					var arrList = []
					if (returnData[0].devices) {
						for (var i in returnData[0].devices) {
							if (returnData[0].devices[i].vmsDeviceTypeCh == 'DTU' || returnData[0].devices[i]
								.vmsDeviceTypeCh == '核心控制器') {

							} else {
								arrList.push(returnData[0].devices[i])
							}
						}
						this.bianhaoList = arrList
					}

				})
			},
			// 添加设备
			addshebei() {
				this.bianhao = ''
				this.roomType = ''
				
				this.serveCode = ''
				this.equipmentCode = ''
				this.roomSelect = ''
				this.musicSelectStart = ''
				this.musicSelectEnd = ''
				this.deviceId = ''
				this.roomWeight = ''
				this.roomNo = ''
				
				this.shebeiSelect()
				this.dialogFormVisible12 = true
			},
			shebeiBtn() {
				for (var i in this.bianhaoList) {
					if (this.roomNo == this.bianhaoList[i].vmsDeviceNo) {
						this.roomName = this.bianhaoList[i].vmsDeviceTypeCh
					}
					if (this.couponform.roomNo == this.bianhaoList[i].vmsDeviceNo) {
						this.roomName = this.bianhaoList[i].vmsDeviceTypeCh
					}
				}

			},
			handcilckaddshebei() {
				if(this.roomType==1){
					if (this.roomNo == '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请选择设备',
							type: 'warning'
						});
						return
					}
					if (this.roomType === '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请选择设备类型',
							type: 'warning'
						});
						return
					}
				}
				if(this.roomType==2){
					if (this.roomNo === '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请输入智能插座mac码',
							type: 'warning'
						});
						return
					}
					if (this.roomName === '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请输入智能插座名称',
							type: 'warning'
						});
						return
					}
				}
				
				this.$http({
					url: this.$http.adornUrl('room/insertRoom'),
					method: 'post',
					data: this.$http.adornData({
						// 'store_id': this.store_id,
						'goodsId': this.goods_id,
						'roomNo': this.roomNo,
						'roomType': this.roomType,
						'roomName': this.roomName,
						'serveCode': this.serveCode,
						'equipmentCode': this.equipmentCode,
						'roomSelect': this.roomSelect,
						'musicSelectStart': this.musicSelectStart,
						'musicSelectEnd': this.musicSelectEnd,
						'deviceId': this.deviceId,
						'roomWeight':this.roomWeight,
						
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
								this.dataSelect()
							}
						})
						this.dialogFormVisible12 = false
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
			//修改设备
			handcilckxiugai(rows) {
				console.log(rows)
				this.couponform.bianhao = rows.roomNo
				this.couponform.equipment_id = rows.roomId
				this.couponform.roomType = Number(rows.roomType)
				this.roomName = rows.roomName
				this.goods_id = rows.goodsId
				this.couponform.roomNo = rows.roomNo
				this.couponform.serveCode = rows.serveCode
				this.couponform.equipmentCode = rows.equipmentCode
				this.couponform.roomSelect = rows.roomSelect
				this.couponform.musicSelectStart = rows.musicSelectStart
				this.couponform.musicSelectEnd = rows.musicSelectEnd
				this.couponform.deviceId = rows.deviceId 
				this.couponform.roomWeight = rows.roomWeight
				
				this.shebeiSelect()
				this.dialogFormVisible11 = true
			},
			handcilckxiugaishebei() {
				this.$http({
					url: this.$http.adornUrl('room/updateRoom'),
					method: 'post',
					data: this.$http.adornData({
						'roomId': this.couponform.equipment_id,
						'roomNo': this.couponform.roomNo,
						'roomType': this.couponform.roomType,
						'roomName': this.roomName,
						'goodsId': this.goods_id,
						'serveCode': this.couponform.serveCode,
						'equipmentCode': this.couponform.equipmentCode,
						'roomSelect': this.couponform.roomSelect,
						'musicSelectStart': this.couponform.musicSelectStart,
						'musicSelectEnd': this.couponform.musicSelectEnd,
						'deviceId': this.couponform.deviceId,
						'roomWeight':this.couponform.roomWeight,
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
								this.dataSelect()
							}
						})
						this.dialogFormVisible11 = false
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
			// 删除设备
			handcilckDel(row) {
				this.$confirm(`确定要删除该设备?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('room/deleteRoomById'),
						method: 'post',
						params: this.$http.adornParams({
							'roomId': row.roomId
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
									this.dataSelect()
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
				})
			},

			// 修改设备
			updatesshebei(rows) {
				console.log(rows)
				this.shebeiSelect1()
				this.dialogFormVisibleshebie = true
				this.goods_id = rows.goodsId
				
				// this.tearoomId = rows.tearoomId
				this.innerCode = rows.innerCode
			},
			// 获取设备列表
			shebeiSelect1() {
				let storeId = this.$route.query.storeId
				this.$http({
					url: this.$http.adornUrl('room/selectGroup'),
					method: 'get',
					params: this.$http.adornParams({
						'innerCode': this.equipment_sn,
					})
				}).then(({
					data
				}) => {
					let returnData = data.data.result;
					this.bianhaoList1 = returnData
				})
			},
			disposeNoticeToshiebei() {
				this.$http({
					url: this.$http.adornUrl('goods/bindInnerCode'),
					method: 'post',
					data: this.$http.adornData({
						// 'type': this.type,
						'goodsId': this.goods_id,
						'innerCode': this.innerCode,
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
								this.shopSelect()
								this.dialogFormVisibleshebie = false
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
		},

		activated() {
			let activeName = this.$route.query.activeName
			this.equipment_sn = this.$route.query.equipment_sn
			this.merchant_id = this.$route.query.merchantId
			this.store_id = this.$route.query.storeId
			if (activeName) {
				this.activeName = activeName
				this.shopSelect()
				this.classSelect()
			}
			this.shopSelect()
		}
	}
</script>
<style scoped>
.devicedetails-page__back {
	display: inline-flex;
	align-items: center;
	margin-bottom: 12px;
	font-size: 14px;
	color: #606266;
	text-decoration: none;
}

.devicedetails-page__back:hover {
	color: #5cc477;
}

.devicedetails-page__back-icon {
	width: 1.2em;
	height: 1.2em;
	margin-right: 4px;
}

.devicedetails-page__filter-input {
	width: 160px;
}

.devicedetails-page__filter-select {
	width: 150px;
}

.devicedetails-page__filter-actions .el-button + .el-button {
	margin-left: 12px;
}

.devicedetails-page__tabs {
	margin-bottom: 0;
}

.devicedetails-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.devicedetails-page__tabs .nf-table-card {
	margin-bottom: 0;
}

.devicedetails-page__tabs >>> .el-tabs__item.is-active {
	color: #5cc477;
}

.devicedetails-page__tabs >>> .el-tabs__active-bar {
	background-color: #5cc477;
}

.devicedetails-page__type-billiard {
	color: #5cc477;
}

.devicedetails-page__type-chess {
	color: #f56c6c;
}

.devicedetails-page__type-other {
	color: #3fae5c;
}

.devicedetails-page__type-cabinet {
	color: sandybrown;
}

.devicedetails-page__price {
	color: #f56c6c;
}

.devicedetails-page__status-clean {
	color: #5cc477;
	cursor: pointer;
}

.devicedetails-page__status-maintain {
	color: orange;
	cursor: pointer;
}

.devicedetails-page__status-closed {
	color: #999;
	cursor: pointer;
}

.devicedetails-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}

.nf-table-card .el-button + .el-button {
	margin-left: 0 !important;
	margin-top: 5px !important;
}

::v-deep .switchClass .el-switch__label {
	position: absolute;
	display: none;
	color: #fff !important;
}

::v-deep .switchClass .el-switch__label--right {
	z-index: 1;
}

::v-deep .switchClass .el-switch__label--right span {
	margin-right: 13px;
	font-size: 12px;
}

::v-deep .switchClass .el-switch__label--left {
	z-index: 1;
}

::v-deep .switchClass .el-switch__label--left span {
	margin-left: 17px;
	font-size: 12px;
}

::v-deep .switchClass .el-switch__label.is-active {
	display: block;
}

::v-deep .switchClass.el-switch .el-switch__core,
.el-switch .el-switch__label {
	width: 42px !important;
	margin: 0;
}
</style>