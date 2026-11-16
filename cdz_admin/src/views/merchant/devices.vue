<template>
	<div class="nf-page devices-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">设备管理</span>
			<h2 class="nf-page-hero__title">设备列表</h2>
			<p class="nf-page-hero__desc">查看与管理台球馆设备绑定，支持按店铺名称、商户昵称筛选，可查询、重置及绑定大门设备、查看监控与一键开关操作。</p>
			<div class="nf-page-hero__bar"></div>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">
				<i class="el-icon-search nf-filter-card__head-icon"></i>
				<span>筛选条件</span>
			</div>
			<el-form :inline="true" class="nf-filter-card__body" @submit.native.prevent>
				<el-form-item label="店铺名称">
					<el-input
						class="devices-page__filter-input"
						@keydown.enter.native="select"
						clearable
						placeholder="请输入店铺名称"
						v-model="storeName">
					</el-input>
				</el-form-item>
				<el-form-item label="商户昵称">
					<el-input
						class="devices-page__filter-input"
						@keydown.enter.native="select"
						clearable
						placeholder="请输入商户昵称"
						v-model="userNameT">
					</el-input>
				</el-form-item>
				<el-form-item class="devices-page__filter-actions">
					<el-button type="primary" icon="el-icon-search" @click="select">查询</el-button>
					<el-button type="primary" icon="el-icon-refresh-left" @click="cleans">重置</el-button>
				</el-form-item>
			</el-form>
		</div>

		<el-tabs v-model="activeName" class="devices-page__tabs">
			<el-tab-pane label="设备列表" name="first">
				<div class="nf-table-card">
		<el-table v-loading="tableDataLoading" :data="tableData.list">
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
			<el-table-column prop="tearoomCoverImage" label="台球馆轮播图" width="200">
				<template slot-scope="scope">
					<div v-if="scope.row.tearoomCoverImage">
						<span v-for="(item,index) in scope.row.tearoomCoverImage.split(',')">
							<el-popover placement="top-start" title="" trigger="hover">
								<img style="width: 50px; height: 50px;margin: 3px;" :src="item" alt="" slot="reference">
								<img style="width: 300px; height: 300px" :src="item" alt="">
							</el-popover>
						</span>
					</div>
				</template>
			</el-table-column>
			<el-table-column prop="store_address" label="台球馆发货地址" width="200">
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
			<el-table-column prop="innerCode" label="设备组编号" width="120">
			</el-table-column>
			<el-table-column prop="roomName" label="设备编号" width="120">
			</el-table-column>
			<el-table-column prop="roomName" label="设备" width="120">
			</el-table-column>
			<el-table-column prop="innerCode" label="绑定设备" width="130">
				<template slot-scope="scope">
					<!-- <span style="color: #5cc477;" v-if="scope.row.innerCode &&scope.row.innerCode!= ''">已绑定</span>
          <span style="color: #F56C6C;" v-else>未绑定</span> -->
					<el-button size="mini" type="primary" plain @click="updatesshebei(scope.row)">绑定大门设备
					</el-button>
				</template>
			</el-table-column>
			<el-table-column label="操作" width="120" fixed="right">
				<template slot-scope="scope">
					<el-button size="mini" type="primary" @click="merchLook(scope.row)">查看台球馆
					</el-button>
					<el-button size="mini" type="primary" @click="merchDetails(scope.row)">台球馆列表
					</el-button>

					<el-button size="mini" type="primary" @click="updatesJiankong(scope.row)">台球馆监控
					</el-button>
					<!-- <el-button size="mini" type="primary" plain @click="cabinetDetails(scope.row)">展示柜列表
					</el-button> -->
					<el-button size="mini" type="warning" plain @click="cabinetClose(scope.row)">一键开关
					</el-button>
				</template>
			</el-table-column>
		</el-table>
					<div class="devices-page__pagination">
						<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
							:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
							layout="total,sizes, prev, pager, next" :total="tableData.totalCount">
						</el-pagination>
					</div>
				</div>
			</el-tab-pane>
		</el-tabs>

		<!-- 查看弹框-->
		<el-dialog title="查看台球馆" :visible.sync="dialogFormVisible" center>
			<el-form :model="form">
				<el-form-item label="台球馆名称：" :label-width="formLabelWidth">
					<el-input v-model="form.storeName" v-if="isshop" disabled style="width:65%;"></el-input>
					<el-input v-model="form.storeName" v-if="!isshop" style="width:65%;"></el-input>
				</el-form-item>
				<el-form-item label="台球馆图片：" :label-width="formLabelWidth">
					<img v-if="isshop" :src="form.logo" class="avatar"
						style="border-radius: 6px;width: 148px;height: 148px;" />
					<div v-if="!isshop"
						style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
						<el-upload class="avatar-uploader" v-model="form.logo"
							:action="$http.adornUrlUp('alioss/upload')" :show-file-list="false"
							:on-success="handleAvatarSuccess">
							<img v-if="form.logo" :src="form.logo" class="avatar"
								style="border-radius: 6px;width: 148px;height: 148px;" />
							<i v-else class="el-icon-plus avatar-uploader-icon"></i>
						</el-upload>
					</div>
				</el-form-item>
				<el-form-item label="台球馆发货地址：" :label-width="formLabelWidth">
					<el-input v-model="form.shipAddress" v-if="isshop" disabled style="width:65%;"></el-input>
					<el-input v-model="form.shipAddress" v-if="!isshop" style="width:65%;"></el-input>
				</el-form-item>
				<el-form-item label="台球馆主办人：" :label-width="formLabelWidth">
					<el-input v-model="form.refundContact" v-if="isshop" disabled style="width:65%;"></el-input>
					<el-input v-model="form.refundContact" v-if="!isshop" style="width:65%;"></el-input>
				</el-form-item>
				<el-form-item label="台球馆手机号：" :label-width="formLabelWidth">
					<el-input v-model="form.refundMobile" v-if="isshop" disabled style="width:65%;"></el-input>
					<el-input v-model="form.refundMobile" v-if="!isshop" style="width:65%;"></el-input>
				</el-form-item>
				<el-form-item label="台球馆开店时间：" :label-width="formLabelWidth">
					<el-input v-model="form.shoptime" v-if="isshop" disabled style="width:65%;"></el-input>
					<el-input v-model="form.shoptime" v-if="!isshop" style="width:65%;"></el-input>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer" v-if="!isshop">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="disposeNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 设备 -->
		<el-dialog title="修改设备" :visible.sync="dialogFormVisibleshebie" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">设备组：</span>
				<el-select v-model="innerCode" placeholder="请选择设备" style="width:50%;" @change="shebeiBtn()">
					<el-option v-for="item in bianhaoList" :key="item.innerCode" :label="item.vmsGroupName"
						:value="item.innerCode">
					</el-option>
				</el-select>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">设备：</span>
				<el-select v-model="roomNo" placeholder="请选择设备" style="width:50%;" @change="shebeiBtn2()">
					<el-option v-for="item in bianhaoList1" :key="item.vmsDeviceNo" :label="item.vmsDeviceTypeCh"
						:value="item.vmsDeviceNo">
					</el-option>
				</el-select>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisibleshebie = false">取 消</el-button>
				<el-button type="primary" @click="disposeNoticeToshiebei()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 台球馆监控 -->
		<el-dialog title="台球馆监控" :visible.sync="dialogFormVisibleJk" center width="1000px">
			<div>
				<iframe v-if="jiankongUrl!=''" :src="jiankongUrl"
					style="width: 800px;height: 500px;margin: 0 70px;"></iframe>
				<!-- <video style="width: 250px;height: 100px;"
				v-if="jiankongUrl!=''" :src="jiankongUrl"
				type="audio/wav" controls="controls"></video> -->
				<div v-else style="text-align: center;">暂无数据</div>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisibleJk = false">关闭弹框</el-button>
				<!-- <el-button type="primary" @click="disposeNoticeToshiebei()">确 定</el-button> -->
			</div>
		</el-dialog>

		<!-- 一键开关设备 -->
		<el-dialog title="一键开关设备" :visible.sync="dialogFormVisibleClose" center width="1000px">
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">修改状态：</span>
				<el-radio-group v-model="type">
					<el-radio :label="1">开启</el-radio>
					<el-radio :label="2">关闭</el-radio>
				</el-radio-group>
			</div>

			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisibleClose = false">取 消</el-button>
				<el-button type="primary" @click="shiebeiClose()">确 定</el-button>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				activeName: 'first',
				primary_equipment_name: '',
				primary_equipment_sn: '',
				equipment_gate_name: '',
				equipment_gate_sn: '',
				primary_argument_one: '',
				primary_argument_two: '',
				primary_argument_thd: '',
				gate_argument_one: '',
				gate_argument_two: '',
				gate_argument_thd: '',
				type: 0,
				size: 10,
				page: 1,
				storeName: '',
				userNameT: '',
				form: {
					storeName: '',
					logo: '',
					shipAddress: '',
					refundContact: '',
					refundMobile: '',
					shoptime: '',
				},
				isshop: true,
				formLabelWidth: '200px',
				tableDataLoading: true,
				dialogFormVisible: false,
				dialogFormVisibleshebie: false,
				tableData: [],
				tearoomId: '',
				bianhaoList: [],
				innerCode: '',
				dialogFormVisibleJk: false,
				equipment_sn: '',
				jiankongUrl: '',
				bianhaoList1: [],
				roomNo: '',
				roomName: '',
				dialogFormVisibleClose: false,

			}
		},
		methods: {
			// 商户详情跳转
			merchDetails(rows) {
				this.$router.push({
					path: '/devicedetails',
					query: {
						merchantId: rows.merchant_id,
						storeId: rows.tearoomId,
						storeName: rows.store_name,
						equipment_sn: rows.innerCode
					}
				});
			},
			// 详情跳转
			updates(userId) {
				if (userId) {
					this.$router.push({
						path: '/userDetail',
						query: {
							userId: userId
						}
					})
				}

			},
			handleSizeChange(val) {
				this.size = val
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val
				this.dataSelect()
			},
			// select自动选择
			selectTrigger() {
				this.dataSelect()
			},
			// 查询
			select() {
				this.dataSelect()
			},
			// 重置
			cleans() {
				this.page = 1
				this.storeName = ''
				this.userNameT = ''
				this.dataSelect()
			},
			// 图标上传
			handleAvatarSuccess(file) {
				this.form.logo = file.data;
			},

			// 查看
			merchLook(row) {
				console.log(row)
				this.dialogFormVisible = true
				this.form.storeName = row.tearoomName
				this.form.logo = row.tearoomTitleImage
				this.form.shipAddress = row.tearoomProvince + row.tearoomCity + row.tearoomDistrict + row.tearoomAddress
				this.form.refundContact = row.userName
				this.form.refundMobile = row.phone
				this.form.shoptime = row.startTime + '-' + row.endTime
			},
			updatesshebei(rows) {


				this.tearoomId = rows.tearoomId
				this.innerCode = rows.innerCode
				this.roomNo = rows.roomNo
				this.roomName = rows.roomName
				this.shebeiSelect()
				this.shebeiSelect1()
				this.dialogFormVisibleshebie = true
			},
			disposeNoticeToshiebei() {
				this.$http({
					url: this.$http.adornUrl('tearoom/insertGoodsGate'),
					method: 'post',
					// data: this.$http.adornData({
					params: this.$http.adornParams({
						// 'type': this.type,
						'tearoomId': this.tearoomId,
						'innerCode': this.innerCode,
						'roomNo': this.roomNo,
						'roomName': this.roomName
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
			// 获取数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('tearoom/selectTearoomList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.size,
						'tearoomName': this.storeName,
						// 'goodsId': this.userNameT,
						'city': '',
						'status': '',
						// url: this.$http.adornUrl('room/selectRoomList'),
						// method: 'get',
						// params: this.$http.adornParams({
						//   'page': this.page,
						//   'limit': this.size,
						//   'roomName': this.storeName,
						//   'goodsId': this.userNameT
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.tableData = returnData
				})
			},
			// 获取设备列表
			shebeiSelect() {
				this.$http({
					url: this.$http.adornUrl('room/selectGroup'),
					method: 'get',
					params: this.$http.adornParams({
						'innerCode': this.equipment_sn,
					})
				}).then(({
					data
				}) => {
					let returnData = data.data;
					this.bianhaoList = returnData.result
				})
			},
			shebeiSelect1() {
				// let storeId = this.$route.query.storeId
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
						this.bianhaoList1 = arrList
					}
					// this.bianhaoList = returnData[0].devices
				})
			},
			shebeiBtn() {
				this.roomNo = ''
				this.shebeiSelect1()

			},
			shebeiBtn2() {
				this.switchDisplay = ''
				this.instructCode = ''
				for (var i in this.bianhaoList1) {
					if (this.roomNo == this.bianhaoList1[i].vmsDeviceNo) {
						this.roomName = this.bianhaoList1[i].vmsDeviceTypeCh
					}
				}
			},
			// 监控弹框
			updatesJiankong(row) {
				this.jiankongUrl = ''
				this.equipment_sn = row.equipment_sn
				if (row.equipment_sn) {
					this.shebeiSelectJ()
				}

				this.dialogFormVisibleJk = true
			},
			// 获取设备列表
			shebeiSelectJ() {
				this.$http({
					url: this.$http.adornUrl('admin/hardware/get_all_hardware_list'),
					method: 'post',
					params: this.$http.adornParams({
						'innerCode': this.equipment_sn,
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						let returnData = data.data;
						if (returnData[0].vmscameras) {
							this.jiankongUrl = returnData[0].vmscameras[0].pcAddress
						} else {
							this.jiankongUrl = ''
						}
					}
				})
			},
			// 跳转展示柜列表
			cabinetDetails(rows) {
				this.$router.push({
					path: '/displayCabinetTwo',
					query: {
						merchantId: rows.merchant_id,
						storeId: rows.tearoomId,
						storeName: rows.store_name,
						equipment_sn: rows.innerCode
					}
				});
			},
			// 一键开关 弹框
			cabinetClose(row) {
				this.tearoomId = row.tearoomId
				this.type = ''
				this.dialogFormVisibleClose = true
			},
			shiebeiClose() {
				this.$http({
					url: this.$http.adornUrl('goods/openOrCloseRoom'),
					method: 'post',
					// data: this.$http.adornData({
					params: this.$http.adornParams({
						'tearoomId': this.tearoomId,
						'type': this.type,
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
								this.dialogFormVisibleClose = false
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
		},
		mounted() {
			this.dataSelect()
		}
	}
</script>

<style scoped>
.devices-page__filter-input {
	width: 160px;
}

.devices-page__filter-actions .el-button + .el-button {
	margin-left: 12px;
}

.devices-page__tabs {
	margin-bottom: 0;
}

.devices-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.devices-page__tabs .nf-table-card {
	margin-bottom: 0;
}

.devices-page__tabs >>> .el-tabs__item.is-active {
	color: #5cc477;
}

.devices-page__tabs >>> .el-tabs__active-bar {
	background-color: #5cc477;
}

.devices-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}

.devices-page .nf-table-card .el-button + .el-button {
	margin-left: 0 !important;
	margin-top: 5px !important;
}
</style>