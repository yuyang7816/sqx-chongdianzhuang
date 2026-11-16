<template>
	<div class="nf-page cabinet-order-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">租柜管理</span>
			<h2 class="nf-page-hero__title">租柜订单</h2>
			<p class="nf-page-hero__desc">查看与管理租柜订单，支持按手机号、用户昵称、租柜名称、状态等筛选，可查询、重置、导出及退款操作。</p>
			<div class="nf-page-hero__bar"></div>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">
				<i class="el-icon-search nf-filter-card__head-icon"></i>
				<span>筛选条件</span>
			</div>
			<el-form :inline="true" class="nf-filter-card__body" @submit.native.prevent>
				<el-form-item label="手机号">
					<el-input
						class="cabinet-order-page__filter-input"
						@keydown.enter.native="select"
						clearable
						placeholder="请输入手机号"
						v-model="ordersType">
					</el-input>
				</el-form-item>
				<el-form-item label="用户昵称">
					<el-input
						class="cabinet-order-page__filter-input"
						@keydown.enter.native="select"
						clearable
						placeholder="请输入用户昵称"
						v-model="userName">
					</el-input>
				</el-form-item>
				<el-form-item label="租柜名称">
					<el-input
						class="cabinet-order-page__filter-input"
						@keydown.enter.native="select"
						clearable
						placeholder="请输入租柜名称"
						v-model="ordersUserName">
					</el-input>
				</el-form-item>
				<el-form-item label="状态">
					<el-select
						class="cabinet-order-page__filter-select"
						v-model="status"
						@change="animeDat(status)">
						<el-option v-for="item in statesnum" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</el-form-item>
				<!-- <el-form-item label="支付方式">
					<el-select
						class="cabinet-order-page__filter-select"
						v-model="orderTypeT"
						@change="animeDat()">
						<el-option v-for="item in typeNum" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="是否需要补费">
					<el-select
						class="cabinet-order-page__filter-select"
						v-model="moneyT"
						@change="animeDat()">
						<el-option v-for="item in typeNum2" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</el-form-item> -->
				<el-form-item label="订单编号">
					<el-input
						class="cabinet-order-page__filter-input"
						@keydown.enter.native="select"
						clearable
						placeholder="请输入订单编号"
						v-model="ordersNoT">
					</el-input>
				</el-form-item>
				<el-form-item label="开始时间">
					<el-date-picker
						class="cabinet-order-page__filter-date"
						v-model="startTime"
						align="right"
						type="datetime"
						format="yyyy-MM-dd"
						value-format="yyyy-MM-dd"
						placeholder="选择开始时间">
					</el-date-picker>
				</el-form-item>
				<el-form-item label="截止时间">
					<el-date-picker
						class="cabinet-order-page__filter-date"
						v-model="endTime"
						align="right"
						type="datetime"
						format="yyyy-MM-dd"
						value-format="yyyy-MM-dd"
						placeholder="选择截止时间">
					</el-date-picker>
				</el-form-item>
				<el-form-item class="cabinet-order-page__filter-actions">
					<el-button type="primary" icon="el-icon-search" @click="select">查询</el-button>
					<el-button type="primary" icon="el-icon-refresh-left" @click="cleans">重置</el-button>
					<el-button type="primary" icon="el-icon-download" @click="exportBtn">导出</el-button>
				</el-form-item>
			</el-form>
		</div>

		<el-tabs v-model="activeName" class="cabinet-order-page__tabs" @tab-click="handleClick">
			<el-tab-pane label="全部订单" name="first">
				<div class="nf-table-card">
					<el-table v-loading="tableDataLoading" :data="tableData.list">
						<el-table-column fixed prop="cabinetOrdersId" label="编号" width="80">
						</el-table-column>
						<el-table-column fixed prop="cabinetName" label="租柜名称" width="160">
						</el-table-column>

						<el-table-column prop="userName" label="用户昵称" width="120">
							<template slot-scope="scope">
								<span class="cabinet-order-page__link" v-if="scope.row.userId"
									@click="updates(scope.row.userId)">{{scope.row.userName ? scope.row.userName : '未绑定'}}</span>
							</template>
						</el-table-column>
						<el-table-column prop="phone" label="手机号" width="120">
						</el-table-column>
						<el-table-column prop="ordersNo" label="订单号" width="200">
						</el-table-column>
						<el-table-column prop="hour" label="租借时间" width="80">
						</el-table-column>
						<el-table-column prop="payMoney" label="支付金额">
							<template slot-scope="scope">
								<span class="cabinet-order-page__money" v-if="scope.row.payMoney">{{scope.row.payMoney}}</span>
								<span v-else>-</span>
							</template>
						</el-table-column>
						<el-table-column prop="payWay" label="支付方式" width="100">
							<template slot-scope="scope">
								<span v-if="scope.row.payWay==1">微信</span>
								<span v-else-if="scope.row.payWay==2">支付宝</span>
								<span v-else-if="scope.row.payWay==3">零钱</span>
								<span v-else>-</span>
							</template>
						</el-table-column>
						<el-table-column prop="refundMoney" label="退款金额" width="160">
						</el-table-column>
						<el-table-column prop="refundRemark" label="退款原因" width="160">
						</el-table-column>
						<el-table-column prop="payTime" label="payTime" width="160">
						</el-table-column>

						<el-table-column fixed="right" prop="status" label="订单状态">
							<template slot-scope="scope">
								<span class="cabinet-order-page__status-active" v-if="scope.row.status === 1 ">租借中</span>
								<span class="cabinet-order-page__status-active" v-if="scope.row.status === 2 ">已归还 </span>
								<span class="cabinet-order-page__status-muted" v-if="scope.row.status === 3 ">已退款 </span>

							</template>
						</el-table-column>
						<el-table-column prop="createTime" label="创建时间" width="160">
						</el-table-column>
						<el-table-column prop="createTime" fixed="right" label="操作" width="160">
							<template slot-scope="scope">

								<el-button size="mini" type="warning" style="margin:3px"  v-if="scope.row.status === 2"
									:disabled="!isAuth('orderAdmin:refund')||scope.row.payMoney==scope.row.refundMoney" @click="refundClickBf(scope.row)">退款
								</el-button>
								<el-button size="mini" v-if="scope.row.status === 1" type="primary" style="margin:3px"
									:disabled="!isAuth('orderAdmin:refund')" @click="jieshuClick(scope.row)">取消订单
								</el-button>

							</template>
						</el-table-column>
					</el-table>
					<div class="cabinet-order-page__summary">
						本页累计支付统计：{{totalMoney.toFixed(2)}}元；
					</div>
					<div class="cabinet-order-page__pagination">
						<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
							:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
							layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
						</el-pagination>
					</div>
				</div>
			</el-tab-pane>
		</el-tabs>

		<!-- 部分退款 金额-->
		<el-dialog title="部分退款" :visible.sync="dialogFormVisibleBftk" center>

			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">退款金额：</span>
				<el-input style="width:50%;" v-model="refundMoney" type="number" min="0" placeholder="请输入退款金额">
				</el-input>
				<div style="color: red;margin-left: 200px;">* 最大可退款金额为：{{payMoney}}</div>
			</div>
			<div style="margin-bottom: 10px;">
				<span
					style="width: 200px;display: inline-block;text-align: right;position: relative;top: -70px;">备注：</span>
				<el-input style="width:50%;" v-model="content" type="textarea" :rows="4" placeholder="请输入备注">
				</el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisibleBftk = false">取 消</el-button>
				<el-button type="primary" @click="replyNoticeTk()">确 定</el-button>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				limit: 10,
				page: 1,
				state: 2,
				status: '',
				userName: '',
				ordersUserName: '',
				type: '',
				id: '',
				statesnum: [{
						value: '',
						label: '全部'
					},
					{
						value: 1,
						label: '租借中'
					},
					{
						value: 2,
						label: '已归还'
					},
					{
						value: 3,
						label: '已退款'
					},
					
				],
				typeNum: [{
						value: '',
						label: '全部'
					},
					{
						value: 1,
						label: '微信'
					},
					{
						value: 2,
						label: '支付宝'
					},
					{
						value: 3,
						label: '零钱'
					},
				],
				moneyT: '',
				typeNum2: [{
						value: '',
						label: '全部'
					},
					{
						value: 1,
						label: '是'
					},
					{
						value: 0,
						label: '否'
					},
				],
				form: {
					id: '',
					title: '',
					url: '',
					type: '',
					state: '',
					types: [{
							value: 'word',
							label: '文字'
						},
						{
							value: 'url',
							label: '链接'
						}
					],
				},
				phone: '',
				content: '',
				startTime: '',
				endTime: '',
				flag: '',
				flags: [{
						value: 1,
						label: '单个用户'
					},
					{
						value: 2,
						label: '全部用户'
					}
				],
				formLabelWidth: '200px',
				activeName: 'first',
				tableDataLoading: false,
				dialogFormVisible1: false,
				dialogFormVisible2: false,
				dialogFormVisible: false,
				tableData: [],
				totalMoney: 0,
				totalCashPledge: 0,
				totalMinuteMoney: 0,
				info: {
					stockDate: this.getNowTime(), //日期
				},
				info2: {
					stockDate2: this.getNowTime2(), //日期
				},
				startTime: '',
				endTime: '',
				ordersType: '',
				ordersNoT: '',
				orderTypeT: '',
				// 新增订单数据
				dialogFormVisibleO: false,
				dialogFormVisibleU: false,
				tableDataLoadingU: false,

				nickName2: '',
				inviterCode: '',
				phone1: '',

				userIds: {}, //用户列表
				sizeU: 10,
				pageU: 1,

				dialogFormVisibleF: false,
				tableDataLoadingF: false,
				classify: '',
				classifyList: [{
						id: '',
						name: '全部'
					},
					{
						id: 1,
						name: '线上'
					},
					{
						id: 2,
						name: '线下'
					}
				],
				classifyIds: '',

				search: '',
				statusId: 0,
				statuss: [{
						id: '',
						title: '全部'
					},
					{
						id: 0,
						title: '进行中'
					},
					{
						id: 1,
						title: '待审核'
					},
					{
						id: 2,
						title: '已下架'
					},
					{
						id: 3,
						title: '拒绝'
					}
				],
				sizeF: 10,
				pageF: 1,
				choicenData: {},
				orderType: 1,
				ordersNo: '',

				fuwuName: '',
				orderNumber: 1,
				payMoney: 0,
				orderTakingMoney: 0,
				orderTakingId: '',
				remarks: '',
				isVip: '',
				userIdss: '',
				nickName1: '',
				isVipT: -1,
				isVipType: [{
						value: -1,
						label: '全部'
					},
					{
						value: 1,
						label: '是'
					},
					{
						value: 0,
						label: '否'
					},
				],

				dialogFormVisibleXd: false,
				times: '',
				is_busy: 0,
				timesList: [{
						roomTime: '00:00',
						type: 0
					}, {
						roomTime: '01:00',
						type: 0
					}, {
						roomTime: '02:00',
						type: 0
					}, {
						roomTime: '03:00',
						type: 0
					},
					{
						roomTime: '04:00',
						type: 0
					}, {
						roomTime: '05:00',
						type: 0
					}, {
						roomTime: '06:00',
						type: 0
					}, {
						roomTime: '07:00',
						type: 0
					},
					{
						roomTime: '08:00',
						type: 0
					}, {
						roomTime: '09:00',
						type: 0
					}, {
						roomTime: '10:00',
						type: 0
					}, {
						roomTime: '11:00',
						type: 0
					},
					{
						roomTime: '12:00',
						type: 0
					}, {
						roomTime: '13:00',
						type: 0
					}, {
						roomTime: '14:00',
						type: 0
					}, {
						roomTime: '15:00',
						type: 0
					},
					{
						roomTime: '16:00',
						type: 0
					}, {
						roomTime: '17:00',
						type: 0
					}, {
						roomTime: '18:00',
						type: 0
					}, {
						roomTime: '19:00',
						type: 0
					},
					{
						roomTime: '20:00',
						type: 0
					}, {
						roomTime: '21:00',
						type: 0
					}, {
						roomTime: '22:00',
						type: 0
					}, {
						roomTime: '23:00',
						type: 0
					},

				],
				xudanTime: '',
				year_time: '',
				orderId: '',
				end_time: '',
				xudanList: [],
				tableDataLoadingX: false,
				dialogFormVisibleXdL: false,
				dialogFormVisibleBz: false,
				dialogFormVisibleVido: false,
				classifyT: '',
				homeData1: [{
						id: '',
						name: '全部'
					},
					{
						id: 1,
						name: '原视频'
					},
					{
						id: 2,
						name: '剪辑视频'
					},
				],
				dialogFormVisiblezt: false,
				tearoomId: '',
				goods_list: [],
				hourPrice: '',
				dialogFormVisibleVip: false,
				ordersId: '',
				qitaData: {},
				dialogFormVisibleBftk: false,
				refundMoney: 0,
				payMoney: 0,
				remark: '',

			}
		},
		
		methods: {
			//处理默认选中当前日期
			getNowTime() {
				var now = new Date()
				var year = now.getFullYear() //得到年份
				var month = now.getMonth() //得到月份
				var date = now.getDate() //得到日期
				month = month + 1
				month = month.toString().padStart(2, '0')
				date = date.toString().padStart(2, '0')
				var defaultDate = `${year}-${month}-${date}`
				return defaultDate
				this.$set(this.info, 'stockDate', defaultDate)
			},
			//处理默认选中当前日期
			getNowTime2() {
				var now = new Date()
				var year = now.getFullYear() //得到年份
				var month = now.getMonth() - now.getMonth() //得到月份
				var date = now.getDate() - now.getDate() + 1 //得到日期
				month = month + 1
				month = month.toString().padStart(2, '0')
				date = date.toString().padStart(2, '0')
				var defaultDate = `${year}-${month}-${date}`
				return defaultDate
				this.$set(this.info, 'stockDate', defaultDate)
			},
			
			handleSizeChange(val) {
				this.limit = val;
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val;
				this.dataSelect()
			},
			
			handleClick(tab, event) {
				if (tab._props.label == '全部订单') {
					this.page = 1
					this.limit = 10
					this.ordersType = 1
					this.orderTypeT = ''
					this.moneyT = ''
					this.dataSelect()
				}

			},
			

			// 获取数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.totalMoney = 0
				this.totalCashPledge = 0
				this.totalMinuteMoney = 0

				this.$http({
					url: this.$http.adornUrl('cabinet/selectCabinetOrdersList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						// 'payWay': this.orderTypeT,
						'phone': this.ordersType,
						'userName': this.userName,
						'cabinetName': this.ordersUserName,
						// 'ordersNo': this.ordersNoT,
						'status': this.status,
						'userId':'',
						// 'startTime': this.startTime,
						// 'endTime': this.endTime,
						// 'money': this.moneyT,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					for (var i in data.data.list) {
						if (data.data.list[i].payMoney) {
							this.totalMoney = this.totalMoney + data.data.list[i].payMoney
						}
						// if (data.data.list[i].cashPledge) {
						// 	this.totalCashPledge = this.totalCashPledge + data.data.list[i].cashPledge
						// }
						// if (data.data.list[i].minuteMoney) {
						// 	this.totalMinuteMoney = this.totalMinuteMoney + data.data.list[i].minuteMoney
						// }

					}
					let returnData = data.data;
					this.tableData = returnData

				})
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
			// 查询
			select() {
				this.page = 1
				this.limit = 10
				this.dataSelect()
			},
			// 重置
			cleans() {
				this.page = 1
				this.userName = ''
				this.ordersType = ''
				this.ordersUserName = ''
				this.ordersNoT = ''
				this.status = ''
				this.orderTypeT = ''
				this.startTime = ''
				this.endTime = ''
				this.moneyT = ''
				this.dataSelect()
			},
			// select选择事件
			animeDat(state) {
				this.page = 1
				this.dataSelect()
			},
			// 删除订单
			deletes(row) {
				let delid = row.ordersId
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`orders/deleteOrders?id=${delid}`),
						method: 'post',
						params: this.$http.adornData({})
					}).then(({
						data
					}) => {
						this.$message({
							message: '删除成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
					})
				})
			},
			
			// 导出
			exportBtn() {
				this.$http({
					url: this.$http.adornUrl('orders/excelOrders'),
					method: 'get',
					responseType: 'blob',
					params: this.$http.adornParams({
						// 'page': page,
						// 'size': this.size,
						'payWay': this.orderTypeT,
						'payPhone': this.ordersType,
						'payUserName': this.userName,
						'goodsName': this.ordersUserName,
						'ordersNo': this.ordersNoT,
						'status': this.status,
						'startTime': this.startTime,
						'endTime': this.endTime,
						'money': this.moneyT,
					})
				}).then(({
					data
				}) => {
					let blob = new Blob([data], {
						type: 'application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
					})
					if (window.navigator.msSaveOrOpenBlob) {
						navigator.msSaveBlob(blob)
					} else {
						let url = window.URL.createObjectURL(blob)
						let elink = document.createElement('a')
						elink.download = '订单列表.xlsx'
						elink.style.display = 'none'
						elink.href = url
						document.body.appendChild(elink)
						elink.click()
						document.body.removeChild(elink)
					}
				})
			},
			
			// 退全款 
			refundClick(row) {
				if(row.refundMoney){
					var payMoney = Number(row.payMoney) -  Number(row.refundMoney) 
				}else{
					var payMoney = Number(row.payMoney) 
				}
				this.$confirm(`确定退款?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('cabinet/refundOrdersMoney'),
						method: 'post',
						params: this.$http.adornParams({
							'ordersNo': row.ordersNo,
							'refundMoney': payMoney,
							'refundRemark': this.content,
						})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '退款成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.dataSelect()
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
				}).catch(() => {})
			},
			// 部分退款
			refundClickBf(row) {
				this.refundMoney = ''
				if(row.refundMoney){
					this.payMoney = Number(row.payMoney) -  Number(row.refundMoney) 
				}else{
					this.payMoney = Number(row.payMoney) 
				}
				this.ordersNo = row.ordersNo
				this.content = ''
				this.dialogFormVisibleBftk = true

			},
			// 确认 部分退款
			replyNoticeTk() {
				if (this.refundMoney === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入退款金额',
						type: 'warning'
					});
					return
				}
				if (Number(this.refundMoney) <= 0) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '退款金额必须大于0',
						type: 'warning'
					});
					return
				}
				if (Number(this.refundMoney) > Number(this.payMoney)) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '退款金额不能大于支付金额',
						type: 'warning'
					});
					return
				}
				this.$http({
					url: this.$http.adornUrl('orders/refundOrdersMoney'),
					method: 'post',
					params: this.$http.adornParams({
						'ordersNo': this.ordersNo,
						'refundMoney': this.refundMoney,
						'refundRemark': this.content,
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$message({
							message: '退款成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dialogFormVisibleBftk = false
								this.dataSelect()
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
			},
			// 结束订单
			jieshuClick(row) {
				this.$confirm(`确定要取消订单?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('cabinet/refundOrdersMoney'),
						method: 'post',
						params: this.$http.adornParams({
							'ordersNo': row.ordersNo,
							'refundMoney':row.refundMoney,
							'refundRemark':'取消订单',
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
								type: 'error',
								duration: 1500,
								onClose: () => {}
							})
						}
					})
				}).catch(() => {})
			},
			

		},
		mounted() {
			this.dataSelect()
		}
	};
</script>

<style scoped>
.cabinet-order-page__filter-input {
	width: 160px;
}

.cabinet-order-page__filter-select {
	width: 150px;
}

.cabinet-order-page__filter-date {
	width: 160px;
}

.cabinet-order-page__filter-actions .el-button + .el-button {
	margin-left: 12px;
}

.cabinet-order-page__tabs {
	margin-bottom: 0;
}

.cabinet-order-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.cabinet-order-page__tabs .nf-table-card {
	margin-bottom: 0;
}

.cabinet-order-page__tabs >>> .el-tabs__item.is-active {
	color: #5cc477;
}

.cabinet-order-page__tabs >>> .el-tabs__active-bar {
	background-color: #5cc477;
}

.cabinet-order-page__link {
	color: #5cc477;
	cursor: pointer;
}

.cabinet-order-page__money {
	color: #3fae5c;
}

.cabinet-order-page__status-active {
	color: #5cc477;
}

.cabinet-order-page__status-muted {
	color: #999999;
}

.cabinet-order-page__summary {
	color: #3fae5c;
	font-size: 14px;
	font-weight: 500;
	margin-top: 12px;
}

.cabinet-order-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}

.nf-table-card .el-button + .el-button {
	margin-left: 0 !important;
	margin-top: 5px !important;
}
</style>
