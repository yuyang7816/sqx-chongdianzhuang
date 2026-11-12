<template>
	<div class="nf-page table-code-page">
		<div class="table-code-page__back">
			<a href="#" @click.prevent="prev" class="table-code-page__back-link">
				<icon-svg name="jiantou" class="table-code-page__back-icon"></icon-svg>
				返回
			</a>
		</div>

		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">门店餐饮</span>
			<h2 class="nf-page-hero__title">桌码管理</h2>
			<p class="nf-page-hero__desc">管理门店桌位与桌码，支持按状态、启用状态、桌位名称筛选查询，可查看桌码二维码及添加、修改、删除桌位。</p>
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
						class="table-code-page__filter-select"
						v-model="statusT"
						placeholder="请选择状态"
						@change="phoneSelect()">
						<el-option v-for="item in taskstates" :key="item.value" :label="item.label"
							:value="item.value">
						</el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="是否启用">
					<el-select
						class="table-code-page__filter-select"
						v-model="orderType"
						placeholder="请选择"
						@change="phoneSelect()">
						<el-option v-for="item in orderTypes" :key="item.value" :label="item.label"
							:value="item.value">
						</el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="桌位名称">
					<el-input
						class="table-code-page__filter-input"
						@keydown.enter.native="phoneSelect"
						clearable
						placeholder="请输入桌位名称"
						v-model="userName">
					</el-input>
				</el-form-item>
				<el-form-item class="table-code-page__filter-actions">
					<el-button type="primary" icon="el-icon-search" @click="phoneSelect">查询</el-button>
					<el-button type="primary" icon="el-icon-refresh-left" @click="cleans2">重置</el-button>
					<el-button type="primary" icon="el-icon-plus" @click="add()">添加</el-button>
				</el-form-item>
			</el-form>
		</div>

		<el-tabs v-model="activeName" @tab-click="handleClick" class="table-code-page__tabs">
			<el-tab-pane label="桌码列表" name="first">
				<div class="nf-table-card">
					<el-table v-loading="tableDataLoading" :data="tableData2.records">
						<el-table-column fixed prop="seatId" label="编号" width="80"></el-table-column>

						<el-table-column prop="seatName" label="桌位名称"></el-table-column>
						<el-table-column prop="sort" label="排序"></el-table-column>

						<el-table-column prop="isEnable" label="是否启用">
							<template slot-scope="scope">
								<span v-if="scope.row.isEnable === 1">启用</span>
								<span v-if="scope.row.isEnable === 0">不启用</span>
							</template>
						</el-table-column>

						<el-table-column fixed="right" prop="status" label="状态">
							<template slot-scope="scope">
								<span class="table-code-page__status-active" v-if="scope.row.status === 1">使用中</span>
								<span class="table-code-page__status-idle" v-if="scope.row.status === 2">空闲中</span>
							</template>
						</el-table-column>
						<el-table-column prop="createTime" label="创建时间" width="100"></el-table-column>
						<el-table-column label="桌码" width="100">
							<template slot-scope="scope">
								<el-button size="mini" type="primary" @click="erweima(scope.row)" style="margin: 5px;">查看
								</el-button>
							</template>
						</el-table-column>
						<el-table-column fixed="right" prop="status" label="操作" width="180">
							<template slot-scope="scope">
								<el-button type="primary" size="mini" :disabled="!isAuth('mission:qucan')"
									@click="compile(scope.row)">
									修改</el-button>
								<el-button size="mini" type="danger" :disabled="!isAuth('mission:delete')"
									@click="tuikuan(scope.row)">
									删除
								</el-button>
							</template>
						</el-table-column>
					</el-table>
					<div class="table-code-page__pagination">
						<el-pagination @size-change="handleSizeChange3" @current-change="handleCurrentChange3"
							:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
							layout="total,sizes, prev, pager, next" :total="tableData2.total">
						</el-pagination>
					</div>
				</div>
			</el-tab-pane>
			<!-- 添加/修改弹框 -->
			<el-dialog :title="titles" :visible.sync="dialogFormVisible7" center>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">桌位名称：</span>
					<el-input style="width:50%;" v-model="seatName" type="text" placeholder="请输入桌位名称"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">排序：</span>
					<el-input style="width:50%;" v-model="sort" type="number" placeholder="请输入排序"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">是否启用：</span>
					<el-radio-group v-model="isEnable">
						<el-radio :label="1">启用</el-radio>
						<el-radio :label="0">不启用</el-radio>
					</el-radio-group>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible7 = false">取 消</el-button>
					<el-button type="primary" @click="refuseto()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 桌码 -->
			<el-dialog :visible.sync="dialogFormVisible2" center title="桌码" @close="closeerweima">
				<div>
					<img :src="erweimaImg" style="width: 50%;margin-left: 25%;" />
				</div>
			</el-dialog>
		</el-tabs>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				limit: 10,
				page: 1,
				totalMoney: 0,
				phone: '',
				statusT: '',
				describes: '',
				token: '',
				taskNum: '',
				taskMoney: '',
				taskstates: [{
						label: '全部',
						value: ''
					},
					{
						label: '空闲中',
						value: 2
					},
					{
						label: '使用中',
						value: 1
					},

				],
				orderType: '',
				orderTypes: [{
						label: '全部',
						value: ''
					},
					{
						label: '启用',
						value: 1
					},
					{
						label: '不启用',
						value: 0
					},
				],
				shopId: '',
				shopList: '',
				sort: 10,
				id: '',
				btnChangeEnable: true,
				helpTakeId: '',
				helpMaintainId: '',
				content: '',
				classifyIcon: '',
				classifyId: '',
				classifyUrl: '',
				classifyName: '',
				classifyDeatilsName: '',
				formLabelWidth: '200px',
				activeName: 'first',
				tableDataLoading: false,
				dialogFormVisible: false,
				dialogFormVisible1: false,
				dialogFormVisible2: false,
				dialogFormVisible3: false,
				dialogFormVisible4: false,
				dialogFormVisible5: false,
				dialogFormVisible6: false,
				dialogFormVisible7: false,
				dialogFormVisible8: false,
				tableData: [],
				tableData2: {},
				form: {
					id: '',
					classifyName: '',
					classifyUrl: '',
					classifyIcon: '',
					describes: ''
				},
				form2: {
					id: '',
					classifyDeatilsName: '',
					classifyUrl: '',
					classifyIcon: '',
					taskNum: '',
					taskMoney: '',
				},
				protectjieData: [],
				protectpaiData: [],
				helpTaskData: [],
				safeguardData: [],
				classifyData: [],
				checkBoxData: [], //多选框选择的值
				checkBoxData2: [], //多选框选择的值
				userName: '',
				orderNumber: '',
				titles: '添加',
				seatName: '',
				isEnable: 1,
				status: 1,
				sort: '',
				seatId: '',
				erweimaImg: '',
			}
		},
		methods: {
			// 详情跳转
			updates(row) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: row.userId
					}
				})
			},

			handleSizeChange3(val) {
				this.limit = val
				this.taskdataSelect()
			},
			handleCurrentChange3(val) {
				this.page = val
				this.taskdataSelect()
			},
			// tabs切换
			handleClick(tab, event) {

				if (tab._props.label == '桌码列表') {
					this.page = 1
					this.limit = 10
					this.statusT = ''
					this.taskdataSelect()
				}
			},


			// 筛选
			phoneSelect() {
				this.page = 1
				this.limit = 10
				this.taskdataSelect()
			},
			// 重置
			cleans2() {
				this.phone = ''
				this.statusT = ''
				this.userName = ''
				this.orderNumber = ''
				this.orderType = ''
				this.page = 1
				this.taskdataSelect()
			},

			// 获取接单数据列表
			taskdataSelect() {
				this.shopId = this.$route.query.shopId
				this.tableDataLoading = true
				this.totalMoney = 0
				this.$http({
					url: this.$http.adornUrl('admin/shopSeat/getShopSeatList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						// 'phone': this.phone,
						'seatName': this.userName,
						'sysUserId': this.orderNumber,
						'status': this.statusT,
						'shopId': this.shopId,
						'isEnable': this.orderType
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.tableData2 = returnData
				})
			},

			// 餐品完成
			wancheng(row) {

				if (row.status == 3) {
					var status1 = 4
				}
				if (row.status == 6) {
					var status1 = 3
				}
				this.$http({
					url: this.$http.adornUrl('admin/order/updateOrder'),
					method: 'post',
					data: this.$http.adornData({
						'status': status1,
						'orderId': row.orderId,
						'orderNumber': row.orderNumber
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
								this.taskdataSelect()
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
			// 返回上一级
			prev() {
				this.$router.back()
			},
			// 删除
			tuikuan(row) {
				this.$confirm(`确定要删除该桌码吗?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					console.log('取定')
					this.$http({
						url: this.$http.adornUrl('admin/shopSeat/deleteShopSeatById'),
						method: 'get',
						params: this.$http.adornParams({
							'seatId': row.seatId
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
									this.taskdataSelect()
								}
							})
						} else {
							this.$message({
								message: data.msg,
								type: 'error',
								duration: 1500,
								onClose: () => {}
							})
						}
					})
				}).catch(() => {
					console.log('取消')
				})

			},

			// 添加弹框
			add() {
				this.titles = '添加'
				this.seatName = ''
				this.isEnable = 1
				// this.status = 1
				this.sort = ''
				this.seatId = ''
				this.dialogFormVisible7 = true
			},
			// 修改一级分类
			compile(rows) {
				this.titles = '修改'
				this.seatName = rows.seatName
				this.isEnable = rows.isEnable
				// this.status = rows.status
				this.sort = rows.sort
				this.seatId = rows.seatId
				this.dialogFormVisible7 = true
			},
			// 修改一级分类确定
			refuseto() {
				if (this.seatName == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入桌位名称',
						type: 'warning'
					})
					return
				}
				if (this.sort == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入排序',
						type: 'warning'
					})
					return
				}

				this.$http({
					url: this.$http.adornUrl('admin/shopSeat/insertSeat'),
					method: 'post',
					// data: this.$http.adornData({
					params: this.$http.adornParams({
						'seatId': this.seatId,
						'seatName': this.seatName,
						'isEnable': this.isEnable,
						// 'status': this.status,
						'shopId': this.shopId,
						'sort': this.sort,
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.dialogFormVisible7 = false
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.taskdataSelect()
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
			closeerweima() {
				this.erweimaImg = ''
			},
			// 查看二维码
			erweima(row) {
				this.$http({
					url: this.$http.adornUrl('invite/shopSeatQr'),
					method: 'get',
					params: this.$http.adornParams({
						'seatId': row.seatId,
					})
				}).then(({
					data
				}) => {
					if (data) {
						this.erweimaImg = data
						this.dialogFormVisible2 = true
						console.log(data, '获取二维码')
					} else {
						this.$message({
							message: '生成二维码失败，请重试',
							type: 'warning',
							duration: 1500,
							onClose: () => {}
						})
					}
				})
			},
		},
		mounted() {
			this.shopId = this.$route.query.shopId
			this.taskdataSelect()

		},
		watch: {
			'$route': 'taskdataSelect'
		}
	}
</script>

<style scoped>
.table-code-page__back {
	margin-bottom: 12px;
}

.table-code-page__back-link {
	display: inline-flex;
	align-items: center;
	font-size: 14px;
	color: #606266;
	text-decoration: none;
}

.table-code-page__back-link:hover {
	color: #5cc477;
}

.table-code-page__back-icon {
	width: 1.2em;
	height: 1.2em;
	margin-right: 4px;
}

.table-code-page__filter-select {
	width: 150px;
}

.table-code-page__filter-input {
	width: 200px;
}

.table-code-page__filter-actions .el-button + .el-button {
	margin-left: 12px;
}

.table-code-page__tabs {
	margin-bottom: 0;
}

.table-code-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.table-code-page__tabs .nf-table-card {
	margin-top: 12px;
	margin-bottom: 0;
}

.table-code-page__tabs >>> .el-tabs__item.is-active {
	color: #5cc477;
}

.table-code-page__tabs >>> .el-tabs__active-bar {
	background-color: #5cc477;
}

.table-code-page__status-active {
	color: #5cc477;
}

.table-code-page__status-idle {
	color: #999;
}

.table-code-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}

.nf-table-card .el-button + .el-button {
	margin-left: 0 !important;
	margin-top: 5px !important;
}
</style>
