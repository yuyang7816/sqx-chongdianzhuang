<template>
	<div class="nf-page cabinet-list-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">租柜管理</span>
			<h2 class="nf-page-hero__title">租柜列表</h2>
			<p class="nf-page-hero__desc">查看与管理门店租柜信息，支持按状态、柜子名称、门店名称筛选，可添加、修改与删除租柜。</p>
			<div class="nf-page-hero__bar"></div>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">
				<i class="el-icon-search nf-filter-card__head-icon"></i>
				<span>筛选条件</span>
			</div>
			<el-form :inline="true" class="nf-filter-card__body" @submit.native.prevent>
				<el-form-item label="状态">
					<el-select
						class="cabinet-list-page__filter-select"
						v-model="shopTypeT"
						@change="selectList()">
						<el-option v-for="item in statesnum" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="柜子名称">
					<el-input
						class="cabinet-list-page__filter-input"
						@keydown.enter.native="selectList"
						clearable
						placeholder="请输入柜子名称"
						v-model="storeNameT">
					</el-input>
				</el-form-item>
				<el-form-item label="门店名称">
					<el-input
						class="cabinet-list-page__filter-input-wide"
						@keydown.enter.native="selectList"
						clearable
						placeholder="请输入门店名称"
						v-model="tearoomNameT">
					</el-input>
				</el-form-item>
				<!-- <el-form-item label="台球馆电话">
					<el-input
						class="cabinet-list-page__filter-input"
						@keydown.enter.native="selectList"
						clearable
						placeholder="请输入台球馆电话"
						v-model="tearoomPhoneT">
					</el-input>
				</el-form-item>
				<el-form-item label="用户昵称">
					<el-input
						class="cabinet-list-page__filter-input"
						@keydown.enter.native="selectList"
						clearable
						placeholder="请输入用户昵称"
						v-model="userNameT">
					</el-input>
				</el-form-item>
				<el-form-item label="是否享受会员权益">
					<el-select
						class="cabinet-list-page__filter-select"
						v-model="isVipPrivilegeT"
						@change="selectList()">
						<el-option v-for="item in isVipPrivilegenum" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</el-form-item> -->
				<el-form-item class="cabinet-list-page__filter-actions">
					<el-button type="primary" icon="el-icon-search" @click="selectList">查询</el-button>
					<el-button type="primary" icon="el-icon-refresh-left" @click="cleans">重置</el-button>
				</el-form-item>
			</el-form>
		</div>

		<div class="nf-table-card">
			<div class="cabinet-list-page__toolbar">
				<el-button
					type="primary"
					icon="document"
					:disabled="!isAuth('cabinetList:add')"
					@click="addtea()">添加</el-button>
			</div>
			<el-table v-loading="tableDataLoading" :data="tableData.list">
			<el-table-column fixed="left" prop="cabinetId" label="编号" width="80">
			</el-table-column>
			<el-table-column prop="tearoomName" fixed="left" label="门店名称" width="120">
			</el-table-column>
			<el-table-column prop="cabinetName" fixed="left" label="柜子名称" width="120">
			</el-table-column>
			<el-table-column prop="cabinetImg" label="柜子图片" width="200">
				<template slot-scope="scope">
					<div v-if="scope.row.cabinetImg">
						<span v-for="(item,index) in scope.row.cabinetImg.split(',')">
							<el-popover placement="top-start" title="" trigger="hover">
								<img style="width: 50px; height: 50px;margin: 3px;" :src="item" alt="" slot="reference">
								<img style="width: 300px; height: 300px" :src="item" alt="">
							</el-popover>
						</span>
					</div>
				</template>
			</el-table-column>
			<el-table-column prop="store_address" label="柜子地址" width="200">
				<template slot-scope="scope">
					<div>
						{{scope.row.province}}{{scope.row.city}}{{scope.row.district}}{{scope.row.address}}
					</div>
					<div>经度：{{scope.row.longitude ? scope.row.longitude : '未绑定'}}</div>
					<div>纬度：{{scope.row.latitude ? scope.row.latitude : '未绑定'}}</div>
				</template>
			</el-table-column>
			<el-table-column prop="startTime" label="开始营业时间">
			</el-table-column>
			<el-table-column prop="endTime" label="结束营业时间">
			</el-table-column>
			<el-table-column prop="price" label="每小时价格">
			</el-table-column>
			<el-table-column prop="dayPrice" label="单日封顶价格">
			</el-table-column>
			<el-table-column prop="status" fixed="right" label="状态" width="80">
				<template slot-scope="scope">
					<span style="color: #5cc477;" v-if="scope.row.status == 1">正常</span>
					<span style="color: #F56C6C;" v-if="scope.row.status == 2">关闭</span>
					<span style="color: #3fae5c;" v-if="scope.row.status == 3">租借中</span>
					<!-- <el-switch v-model="scope.row.ban_status" @change="change(scope.row.ban_status,scope.row)"
						:active-value="openValue" :inactive-value="closeValue" active-color="#13ce66"
						inactive-color="#ff4949">
					</el-switch> -->
				</template>
			</el-table-column>
			<el-table-column prop="createTime" label="创建时间" width="160">
			</el-table-column>
			<el-table-column label="操作" width="160" fixed="right">
				<template slot-scope="scope">
					<el-button size="mini" type="primary" :disabled="!isAuth('cabinetList:update')" @click="addtea(scope.row)">修改
					</el-button>
					<el-button size="mini" type="danger"
						:disabled="!isAuth('cabinetList:delete')" @click="deleteBtn(scope.row)">删除
					</el-button>
					<!-- <el-button size="mini" type="danger" plain v-if="scope.row.status ==3"
						:disabled="!isAuth('cabinetList:update')" @click="deblocking(scope.row,2)">完成
					</el-button> -->
					
					
				</template>
			</el-table-column>
			</el-table>
			<div class="cabinet-list-page__pagination">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
					layout="total,sizes, prev, pager, next" :total="tableData.totalCount">
				</el-pagination>
			</div>
		</div>
		
		<!-- 添加弹框 -->
		<el-dialog :title="titles" :visible.sync="dialogFormVisible1" center width="80%">
			<el-form>
				<div class="shop_item">
					<div class="shop_content" style="max-width: 800px;margin: auto;">
						<div class="content_item" style="margin-bottom: 10px;">
							<span style="width: 120px;text-align: left;display:inline-block;">门店：</span>
							<el-input v-model="tearoomName" @focus="shopselect" style="width:50%;" placeholder="请选择门店">
							</el-input>
						</div>
						<div class="content_item" style="margin-bottom: 10px;">
							<div style="display: flex;align-items: center;margin: 2% 0;">
								<span style="display: inline-block;text-align: left;width: 120px;">柜子图片：</span>
								<div style="display: flex;">
									<div v-for="(item,index) of cabinetImg" :key="index"
										style="display: inline-block;">
										<div style="position: relative;margin: 5px;">
											<img :src="item" class="avatar"
												style="width:148px;height: 148px;border-radius: 6px;" />
											<div @click="clear(index)" class="divhove" v-if="cabinetImg">
												<i class="el-icon-delete"
													style="margin: 0;font-size: 18px;color: #fff;"></i>
											</div>
										</div>
									</div>
									<el-upload class="avatar-uploader"
										style="width:148px;height:148px;background-color: #fbfdff; border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;"
										:action="$http.adornUrlUp('alioss/upload')" :show-file-list="false"
										:on-success="handleUploadSuccess" :on-change="handleChange">
										<i class="el-icon-plus"></i>
									</el-upload>
								</div>
							</div>
						</div>
						<div class="content_item" style="margin-bottom: 10px;">
							<span style="width: 120px;text-align: left;display:inline-block;">柜子名称:</span>
							<el-input style="width:50%;" class="margin15" placeholder="请输入柜子名称" v-model="cabinetName"
								autosize></el-input>
						</div>
						
						<div class="content_item" style="margin-bottom: 10px;">
							<span style="display: inline-block;text-align: left;width: 120px;">每小时价格:</span>
							<el-input style="width:50%;" class="margin15" placeholder="请输入每小时价格"
								v-model="price" type="number" :min="0" autosize></el-input>
						</div>
						<div class="content_item" style="margin-bottom: 10px;">
							<span style="display: inline-block;text-align: left;width: 120px;">单日封顶价格:</span>
							<el-input style="width:50%;" class="margin15" placeholder="请输入单日封顶价格"
								v-model="dayPrice" autosize></el-input>
						</div>
						<div class="content_item" style="position: relative;margin: 10px 0;">
							<span style="display: inline-block;text-align: left;width: 120px;">状态:</span>
							<div class="margin15" style="position: absolute;top: 0;">
								<el-radio-group v-model="status" style="margin-left: 123px;">
									<el-radio :label="1">正常</el-radio>
									<el-radio :label="2">关闭</el-radio>
									<el-radio :label="3">租借中</el-radio>
								</el-radio-group>
							</div>
						</div>
						
						<!-- <div class="content_item" style="margin-bottom: 10px;">
							<span span style="display: inline-block;text-align: left;width: 120px;">城市：</span>
							<el-cascader style="width:50%" size="large" :options="options" ref="cascaderAddr"
								v-model="storeAddress" :placeholder="storeAddre" @change="handleChange55"
								:value="storeAddre">
							</el-cascader>
						</div>
						<div class="content_item" style="margin-bottom: 10px;">
							<span style="display: inline-block;text-align: left;width: 120px;">详细地址:</span>
							<el-input style="width:50%;" class="margin15" placeholder="请输入详细地址"
								v-model="address" autosize @keydown.enter.native="selectDz"></el-input>
							<el-button type="info" plain @click="selectDz()">获取经纬度</el-button>
						</div>
						<div class="content_item" style="margin-bottom: 10px;">
							<span style="display: inline-block;text-align: left;width: 120px;">经度:</span>
							<el-input style="width:50%;" class="margin15" disabled placeholder="请输入经度"
								v-model="longitude" autosize></el-input>
						</div>
						<div class="content_item" style="margin-bottom: 10px;">
							<span style="display: inline-block;text-align: left;width: 120px;">纬度:</span>
							<el-input style="width:50%;" class="margin15" disabled placeholder="请输入纬度"
								v-model="latitude" autosize></el-input>
						</div> -->
						<div class="content_item" style="margin-bottom: 10px;">
							<span style="display: inline-block;text-align: left;width: 120px;">开始营业时间：</span>
							<!-- <el-input v-model="openTime" placeholder="请输入排序号" type="data" min="0" style="width:50%;"></el-input> -->
							<el-time-select v-model="startTime" :picker-options="{
						  start: '00:00',
						  step: '00:15',
						  end: '24:00'
						}" placeholder="选择开始营业时间" style="width:50%;">
							</el-time-select>
						</div>
						<div class="content_item" style="margin-bottom: 10px;">
							<span style="display: inline-block;text-align: left;width: 120px;">结束营业时间：</span>
							<!-- <el-input v-model="closeTime" placeholder="请输入排序号" type="data" min="0" style="width:50%;"></el-input> -->
							<el-time-select v-model="endTime" :picker-options="{
						  start: '00:00',
						  step: '00:15',
						  end: '24:00'
						}" placeholder="选择结束营业时间" style="width:50%;">
							</el-time-select>
						</div>
						<!-- <div>
							<div id="container1" style="width:80%;height:500px;margin-left: 10%;"></div>
						</div> -->
					</div>
				</div>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible1 = false">取 消</el-button>
				<el-button type="primary" @click="addteaclassroom()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 用户列表 -->
		<el-dialog title="用户列表" :visible.sync="dialogFormVisible3" center>
			<div style="margin:2% 0;display: inline-block;">
				<span>手机号：</span>
				<el-input style="width: 150px;" @keydown.enter.native="userclick" clearable placeholder="请输入手机号"
					v-model="phone1"></el-input>&nbsp;&nbsp;&nbsp;&nbsp;
				<span>昵称：</span>
				<el-input style="width: 150px;" @keydown.enter.native="userclick" clearable placeholder="请输入昵称"
					v-model="nickName2"></el-input>
				<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="userclick">
					查询
				</el-button>
				<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="userclose">
					重置
				</el-button>
			</div>
			<el-table width="780px" v-loading="tableDataLoading4" :data="userIds.list">
				<el-table-column prop="userId" fixed label="编号" width="80">
				</el-table-column>
				<el-table-column prop="userName" label="昵称">
					<template slot-scope="scope">
						<span style="color: #f56c6c;">{{scope.row.userName ? scope.row.userName : '未设置'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="phone" label="手机号">
					<template slot-scope="scope">
						<span>{{scope.row.phone ? scope.row.phone : '未绑定'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间">
				</el-table-column>
				<el-table-column label="操作" fixed='right'>
					<template slot-scope="scope">
						<el-button size="mini" type="primary" @click="confirm(scope.row)">确定
						</el-button>

					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChange2" @current-change="handleCurrentChange2"
					:page-sizes="[5, 10, 15, 20]" :page-size="size1" :current-page="page1"
					layout="total,sizes, prev, pager, next,jumper" :total="userIds.totalCount">
				</el-pagination>
			</div>
		</el-dialog>
		<!-- 商家列表  -->
		<el-dialog title="商家列表" :visible.sync="dialogFormVisibleBh" center width="80%">
			<div style="position: relative;display: inline-block;margin:5px;">
				<span>店铺名称:</span>
				<el-input style="width: 200px;" @keydown.enter.native="buhuoBtns" placeholder="请输入店铺名称"
					v-model="shopNameB">
				</el-input>&nbsp;&nbsp;
			</div>
			<div style="position: relative;display: inline-block;margin:5px;">
				<span>手机号:</span>
				<el-input style="width: 200px;" @keydown.enter.native="buhuoBtns" placeholder="请输入手机号" v-model="phoneB">
				</el-input>&nbsp;&nbsp;
			</div>
			<el-button size="mini" type="primary" style="margin: 3px;" @click="buhuoBtns()">查询
			</el-button>
			<el-table v-loading="tableDataLoadingB" :data="baomingData.list">
				<el-table-column fixed="left" prop="tearoomId" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="tearoomName" fixed="left" label="台球馆名称" width="120">
				</el-table-column>
				<el-table-column prop="tearoomTitleImage" label="台球馆logo">
					<template slot-scope="scope">
						<span v-if="scope.row.tearoomTitleImage == null">暂无图片</span>
						<img v-else :src="scope.row.tearoomTitleImage" alt="" width="40" height="40">
					</template>
				</el-table-column>
				<el-table-column prop="nickname" label="用户昵称" width="120">
					<template slot-scope="scope">
						<span style="color: #5cc477;cursor: pointer;"
							@click="updatesvideo(scope.row.userId)">{{scope.row.userName ? scope.row.userName : '未绑定'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="store_address" label="台球馆地址" width="200">
					<template slot-scope="scope">
						<div>
							{{scope.row.tearoomProvince}}{{scope.row.tearoomCity}}{{scope.row.tearoomDistrict}}{{scope.row.tearoomAddress}}
						</div>
						<div>经度：{{scope.row.tearoomLongitude ? scope.row.tearoomLongitude : '未绑定'}}</div>
						<div>纬度：{{scope.row.tearoomLatitude ? scope.row.tearoomLatitude : '未绑定'}}</div>
					</template>
				</el-table-column>
				<el-table-column prop="startTime" label="营业时间" width="100">
					<template slot-scope="scope">
						<div>{{scope.row.startTime}}--{{scope.row.endTime}}</div>
					</template>
				</el-table-column>
				<el-table-column prop="sales" label="销量">
					<template slot-scope="scope">
						<span>{{scope.row.sales ? scope.row.sales : '0'}}</span>
					</template>
				</el-table-column>
		
				<el-table-column fixed="right" label="操作" width="160">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" @click="xzShop(scope.row)"
							style="margin: 3px;">选择</el-button>
		
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChangeBh" @current-change="handleCurrentChangeBh"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="limitB" :current-page="pageB"
					layout="total,sizes, prev, pager, next,jumper" :total="baomingData.totalCount">
				</el-pagination>
			</div>
		</el-dialog>
		
	</div>
</template>

<script>
	import {
		jsonp
	} from 'vue-jsonp'
	import {
		provinceAndCityData,
		regionData,
		provinceAndCityDataPlus,
		regionDataPlus,
		CodeToText,
		TextToCode
	} from 'element-china-area-data'
	var geocoder, map, markersArray = [];
	export default {
		data() {
			return {
				
				options: regionData,
				storeAddress: [],
				storeAddre: '请选择城市',
				storeNameT: '',
				tearoomPhoneT:'',
				tearoomNameT:'',
				banstatus: '',
				size: 10,
				page: 1,
				userNameT: '',
				tableDataLoading: true,
				dialogFormVisible1: false,
				tableData: {},
				userId: '',
				phone1: '',
				nickName1: '', //门店用户
				nickName2: '',
				userIds: {}, //用户列表
				size1: 10,
				page1: 1,
				dialogFormVisible3: false,
				tableDataLoading4: false,
				openValue: 1,
				closeValue: 2,
				shopTypeT:'',
				isVipPrivilegeT:'',
				statesnum: [{
						value: '',
						label: '全部'
					},
					{
						value: 1,
						label: '正常'
					},
					{
						value: 2,
						label: '关闭'
					},
					{
						value: 3,
						label: '租借中'
					},
				],
				isVipPrivilegenum: [{
						value: '',
						label: '全部'
					},
					{
						value: 1,
						label: '是'
					},
					{
						value: 2,
						label: '否'
					},
				],
				// -------------
				titles:'添加租柜',
				cabinetId: 1,
				cabinetName: "", //柜子名称
				cabinetImg: "", //柜子图片
				province: "", //省份
				city: "", //市
				district: "", //区
				address: "", //详细地址
				longitude: "", //经度
				latitude: "", //纬度
				startTime: "", //开始营业时间
				endTime: "", //结束营业时间
				price: 1, //每小时价格
				dayPrice: 1, //单日封顶价格
				status: 1, //状态1正常 2关闭 3租借中
				
				pageB: 1,
				limitB: 10,
				tearoomId: '',
				tearoomName: '',
				phoneB: '',
				shopNameB: '',
				baomingData: {},
				dialogFormVisibleSh: false,
				tableDataLoadingB: false,
				dialogFormVisibleBh:false,
				
			}
		},
		methods: {
			
			//上传成功
			handleUploadSuccess(file, fileList) {
				this.cabinetImg.push(file.data)
			},
			handleChange(e) {
				console.log(e)
			},
			
			// 删除
			clear(index) {
				this.cabinetImg.splice(index, 1);
			},
			
			handleSizeChange(val) {
				this.size = val
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val
				this.dataSelect()
			},
			handleSizeChange2(val) {
				this.size1 = val
				this.userClass()
			},
			handleCurrentChange2(val) {
				this.page1 = val
				this.userClass()
			},
			
			// 查询
			selectList() {
				this.page = 1
				this.dataSelect()
			},
			// 重置
			cleans() {
				this.page = 1
				this.storeNameT = ''
				this.tearoomNameT = ''
				this.tearoomPhoneT = ''
				this.userNameT = ''
				this.shopTypeT = ''
				this.isVipPrivilegeT = ''
				this.dataSelect()
			},
			
			// 封禁/解封
			deblocking(row, index) {
				if (index == 1) {
					this.banStatus = 2
				} else if (index == 2) {
					this.banStatus = 3
				} else {
					this.banStatus = 1
				}
				this.$confirm(`确定执行此操作?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('tearoom/updateTearoom'),
						method: 'post',
						data: this.$http.adornParams({
							'tearoomId': row.tearoomId,
							'status': this.banStatus
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
						}
					})
				}).catch(() => {})
			},
			
			// 获取数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('cabinet/selectCabinetList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.size,
						'cabinetName': this.storeNameT,
						'userName': this.userNameT,
						'city': '',
						'status': '',
						'tearoomName':this.tearoomNameT,
						'status':this.shopTypeT,
						// 'isVipPrivilege':this.isVipPrivilegeT,

					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.tableData = returnData
				})
			},
			// 修改状态
			change(val, row) {
				this.$http({
					url: this.$http.adornUrl('admin/storecomplain/status_store'),
					method: 'post',
					params: this.$http.adornParams({
						'store_id': row.store_id,
						'status': row.status
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
							onClose: () => {
								this.dataSelect()
							}
						})
					}
				})
			},
			
			// 获取省市区
			handleChange55(value) {
				console.log(this.$refs['cascaderAddr'].currentLabels)
				value = this.$refs['cascaderAddr'].currentLabels
				if (value) {
				
					this.province = value[0]
					this.city = value[1]
					this.district = value[2]
					// this.form.city = value[1]
					if (this.city == '市辖区') {
						this.city = this.province
					}
				}
				
			},
			addtea(row) {
				if(row){
					this.titles = '修改租柜'
					this.cabinetId = row.cabinetId
					this.cabinetName = row.cabinetName
					if(row.cabinetImg){
						this.cabinetImg = row.cabinetImg.split(',')
					}else{
						this.cabinetImg = []
					}
					this.tearoomId = row.tearoomId
					this.tearoomName = row.tearoomName
					
					this.province = row.province
					this.city = row.city
					this.district = row.district
					this.address = row.address
					this.longitude = row.longitude
					this.latitude = row.latitude
					this.startTime = row.startTime
					this.endTime = row.endTime
					this.price = row.price
					this.dayPrice = row.dayPrice
					this.status = row.status
					this.storeAddress = []
					this.storeAddre = this.province+'/'+this.city+'/'+this.district
				}else{
					this.titles = '添加租柜'
					this.cabinetId = ''
					this.cabinetName = ''
					this.cabinetImg = []
					this.province = ''
					this.city = ''
					this.district = ''
					this.address = ''
					this.longitude = ''
					this.latitude = ''
					this.startTime = ''
					this.endTime = ''
					this.price = ''
					this.dayPrice = ''
					this.status = 1
					this.storeAddress = []
					this.storeAddre = '请选择城市'
					this.tearoomId = ''
					this.tearoomName = ''
				}
				
				
				// this.getMyLocation();
				this.dialogFormVisible1 = true
			},
			addteaclassroom() {
				
				if (this.tearoomId == ''||this.tearoomId==null) {
					this.$notify({
						message: '请选择门店',
						type: 'error',
						duration: 1500
					})
					return
				}
				if (this.cabinetName == '') {
					this.$notify({
						message: '请填写柜子名称',
						type: 'error',
						duration: 1500
					})
					return
				}
				
				if (this.cabinetImg.length == 0) {
					this.$notify({
						message: '请添加柜子图片',
						type: 'error',
						duration: 1500
					})
					return
				}
				
				if (this.address == '') {
					this.$notify({
						message: '请完善位置,经纬度进行选择',
						type: 'error',
						duration: 1500
					})
					return
				}
				if (this.startTime == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择开始营业时间',
						type: 'warning'
					})
					return
				}
				if (this.endTime == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择结束营业时间',
						type: 'warning'
					})
					return
				}
				
				if (this.price === '') {
					this.$notify({
						message: ' 每小时价格不能为空',
						type: 'warning',
						duration: 1500
					})
					return
				}
				if (this.dayPrice === '') {
					this.$notify({
						message: ' 单日封顶价格不能为空',
						type: 'warning',
						duration: 1500
					})
					return
				}
				
				if (this.status === '') {
					this.$notify({
						message: '请选择状态',
						type: 'warning',
						duration: 1500
					})
					return
				}
				if(this.titles == '添加租柜'){
					var url = 'cabinet/insertCabinet'
				}else{
					var url = 'cabinet/updateCabinet'
				}
				this.$http({
					url: this.$http.adornUrl(url),
					method: 'post',
					data: this.$http.adornParams({
						'cabinetId': this.cabinetId,
						'cabinetName': this.cabinetName, //柜子名称
						'cabinetImg': this.cabinetImg.toString(), //柜子图片
						'province': this.province, //省份
						'city': this.city, //市
						'district': this.district, //区
						'address': this.address, //详细地址
						'longitude': this.longitude, //经度
						'latitude': this.latitude, //纬度
						'startTime': this.startTime, //开始营业时间
						'endTime': this.endTime, //结束营业时间
						'price': this.price, //每小时价格
						'dayPrice': this.dayPrice, //单日封顶价格
						'status': this.status, //状态1正常 2关闭 3租借中
						'tearoomId': this.tearoomId,
						
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$message({
							message: '添加成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dialogFormVisible1 = false
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
			},
			//定位获得当前位置信息
			getMyLocation() {
				var geolocation = new qq.maps.Geolocation("G5GBZ-NJ664-5KUUL-FBXE5-PQ5HK-C4BPJ", "充电桩");
				geolocation.getIpLocation(this.showPosition, this.showErr);
				// geolocation.getLocation(this.showPosition, this.showErr);//或者用getLocation精确度比较高
			},
			showPosition(position) {
				console.log(position);
				this.setMap();
			},
			showErr(e) {
				console.log("定位失败", e);
				this.getMyLocation(); //定位失败再请求定位，测试使用
			},
			//位置信息在地图上展示
			setMap() {
				//步骤：定义map变量 调用 qq.maps.Map() 构造函数   获取地图显示容器
				//设置地图中心点
				var myLatlng = new qq.maps.LatLng(this.latitude, this.longitude);
				//定义工厂模式函数
				var myOptions = {
					zoom: 13, //设置地图缩放级别
					center: myLatlng, //设置中心点样式
					mapTypeId: qq.maps.MapTypeId.ROADMAP //设置地图样式详情参见MapType
				}
				// //获取dom元素添加地图信息
				var map = new qq.maps.Map(document.getElementById("container1"), myOptions);
				//给地图添加点击事件

				//给定位的位置添加图片标注
				var marker = new qq.maps.Marker({
					position: myLatlng,
					map: map
				});
				// `````````````
				var that = this;
				if (that.longitude == '') {
					var center = new qq.maps.LatLng(34.34281541842994, 108.93970884382725);
				} else {
					var center = new qq.maps.LatLng(that.latitude, that.longitude);
				}
				var map = new qq.maps.Map(document.getElementById("container1"), {
					center: center,
					zoom: 13
				});
				var marker = new qq.maps.Marker({
					position: center,
					map: map
				});
				var latlngBounds = new qq.maps.LatLngBounds();
				qq.maps.event.addListener(map, "click", function(event) {
					console.log(event, qq.maps);
					that.longitude = event.latLng.getLng(); // 经度
					that.latitude = event.latLng.getLat(); // 纬度
					jsonp('https://apis.map.qq.com/ws/geocoder/v1/?location=' + event.latLng.getLat() + ',' + event
						.latLng
						.getLng() + '&key=ZBABZ-ZWECU-UQTVV-4LYDR-COK3F-5SF75&get_poi=1&output=jsonp', {
							myCustomUrlParam: 'veryNice'
						}).then(response => {
						console.log('response', response)
						that.address = response.result.formatted_addresses.recommend
						that.province = response.result.ad_info.province
						that.city = response.result.ad_info.city
						that.district = response.result.ad_info.district
						

					}).catch(error => {
						// handle error
					}).then(() => {
						// always executed
					});
					if (markersArray) {
						for (let i in markersArray) {
							markersArray[i].setMap(null);
						}
					}
					var marker = new qq.maps.Marker({
						map: map,
						position: event.latLng
					});
					markersArray.push(marker);
				});
				geocoder = new qq.maps.Geocoder({
					complete: function(result) {
						console.log(result);
						that.longitude = result.detail.location.lng;
						that.latitude = result.detail.location.lat;
						map.setCenter(result.detail.location);
						var marker = new qq.maps.Marker({
							map: map,
							position: result.detail.location
						});
						markersArray.push(marker);
					}
				});
			},
			// 地图定位
			selectDz() {
				
				console.log('搜索地址：', this.province + this.city + this.district + this.address)
				var addr = this.province + this.city + this.district + this.address //
				let that = this
				jsonp('https://apis.map.qq.com/ws/geocoder/v1/?address==' + addr +
					'&key=ZBABZ-ZWECU-UQTVV-4LYDR-COK3F-5SF75&get_poi=1&output=jsonp', {
						myCustomUrlParam: 'veryNice'
					}).then(response => {
					// handle success
					console.log('response', response)
					that.longitude = response.result.location.lng; // 经度
					that.latitude = response.result.location.lat; // 纬度
					// that.address = response.result.title
					that.setMap()
				}).catch(error => {
					// handle error
				}).then(() => {
					// always executed
				});
			},
			// 获取用户列表弹框
			userselect() {
				this.dialogFormVisible3 = true
				this.tableDataLoading4 = true
				this.page1 = 1
				this.phone1 = ''
				this.nickName2 = ''
				this.userClass()
			},
			// 获取用户列表
			userClass() {
				this.tableDataLoading4 = true
				this.$http({
					url: this.$http.adornUrl('user/selectUserList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page1,
						'limit': this.size1,
						'userName': this.nickName2,
						'inviterCode': '',
						'startTime': '',
						'endTime': '',
						'isAuthentication': '',
						'phone': this.phone1
					})

				}).then(({
					data
				}) => {
					this.tableDataLoading4 = false
					let returnData = data.data
					this.userIds = returnData
				})
			},
			// 查询用户列表
			userclick() {
				this.page1 = 1
				this.userClass()
			},
			// 重置用户列表数据
			userclose() {
				this.phone1 = ''
				this.nickName2 = ''
				this.userClass()
			},
			// 确定用户
			confirm(row) {
				this.userId = row.userId
				this.nickName1 = row.userName
				this.dialogFormVisible3 = false
			},
			
			deleteBtn(row) {
				this.$confirm(`确定删除此数据?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('cabinet/deleteCabinet'),
						method: 'post',
						params: this.$http.adornParams({
							'cabinetId': row.cabinetId,
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
						}else{
							this.$message({
								message:data.msg,
								type: 'warning',
								duration: 1500,
								onClose: () => {
								}
							})
						}
					})
				}).catch(() => {})
			},
			// 商家弹框
			shopselect() {
				this.pageB = 1
				this.phoneB = ''
				this.shopNameB = ''
			
				this.dialogFormVisibleBh = true
				this.buhuoSelect()
			},
			buhuoSelect() {
				this.tableDataLoadingB = true
				this.$http({    //tearoom/selectTearoomList?page=1&limit=10&tearoomName=&userName=&city=&status=
					url: this.$http.adornUrl('tearoom/selectTearoomList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.pageB,
						'limit': this.limitB,
						'userName': this.userNameT,
						// 'phone': this.phoneB,
						'tearoomName': this.shopNameB,
						'status': '',
						'city': '',
			
					})
			
			
				}).then(({
					data
				}) => {
					console.log('', data)
					this.tableDataLoadingB = false
					if (data.code == 0) {
						let returnData = data.data
						this.baomingData = returnData
					} else {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: data.msg,
							type: 'warning'
						});
					}
				})
			},
			handleSizeChangeBh(val) {
				this.limitB = val
				this.buhuoSelect()
			},
			handleCurrentChangeBh(val) {
				this.pageB = val
				this.buhuoSelect()
			},
			xzShop(row) {
				
				this.tearoomName = row.tearoomName
				this.tearoomId = row.tearoomId
				this.province = row.tearoomProvince
				this.city = row.tearoomCity
				this.district = row.tearoomDistrict
				this.address = row.tearoomAddress
				this.longitude = row.tearoomLongitude
				this.latitude = row.tearoomLatitude
				
				this.dialogFormVisibleBh = false
			},
		},
		mounted() {
			this.dataSelect()
		}
	}
</script>

<style scoped>
.cabinet-list-page__filter-select {
	width: 150px;
}

.cabinet-list-page__filter-input {
	width: 160px;
}

.cabinet-list-page__filter-input-wide {
	width: 200px;
}

.cabinet-list-page__filter-actions .el-button + .el-button {
	margin-left: 12px;
}

.cabinet-list-page__toolbar {
	display: flex;
	justify-content: flex-end;
	margin-bottom: 12px;
}

.cabinet-list-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}

.nf-table-card .el-button + .el-button {
	margin-left: 0 !important;
	margin-top: 5px !important;
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
		-webkit-transition: opacity .3s;
		transition: opacity .3s;
	}
	
	.el-tag--medium {
		margin-right: 10px;
	}
	
	.divhove:hover {
		opacity: 0.8;
	}
	
	.text_color span {
		margin-right: 5px;
	}
	
	.shop_item {
		/* margin-top: 60px; */
		padding-left: 20px;
	}
	
	/* 规格 */
	.customWidth {
		width: 60% !important;
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
	
	/* switch按钮样式 */
	::v-deep .switchClass .el-switch__label {
		position: absolute;
		display: none;
		color: #fff !important;
	}
	
	/*打开时文字位置设置*/
	::v-deep .switchClass .el-switch__label--right {
		z-index: 1;
	}
	
	/* 调整打开时文字的显示位子 */
	::v-deep .switchClass .el-switch__label--right span {
		margin-right: 13px;
		font-size: 12px;
	
	}
	
	/*关闭时文字位置设置*/
	::v-deep .switchClass .el-switch__label--left {
		z-index: 1;
	}
	
	/* 调整关闭时文字的显示位子 */
	::v-deep .switchClass .el-switch__label--left span {
		margin-left: 17px;
		font-size: 12px;
	}
	
	/*显示文字*/
	::v-deep .switchClass .el-switch__label.is-active {
		display: block;
	}
	
	/* 调整按钮的宽度 */
	::v-deep .switchClass.el-switch .el-switch__core,
	.el-switch .el-switch__label {
		width: 42px !important;
		margin: 0;
	}
</style>