<template>
	<div class="nf-page order-in-service-jl-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">教练订单</span>
			<h2 class="nf-page-hero__title">教练服务中订单</h2>
			<p class="nf-page-hero__desc">查看与管理服务进行中的教练订单，支持按订单类型、教练/用户信息、超时与警告状态筛选，可查询、重置及完成订单操作。</p>
			<div class="nf-page-hero__bar"></div>
		</div>

		<el-tabs v-model="activeName" @tab-click="handleClick" class="order-in-service-jl-page__tabs">
		<el-tab-pane label="订单列表" name="first">
			<div class="nf-filter-card">
				<div class="nf-filter-card__head">
					<i class="el-icon-search nf-filter-card__head-icon"></i>
					<span>筛选条件</span>
				</div>
				<el-form :inline="true" class="nf-filter-card__body" @submit.native.prevent>
					<el-form-item label="订单类型">
						<el-select class="order-in-service-jl-page__filter-select" v-model="ordersType" placeholder="请选择类型" @change="select()">
							<el-option v-for="item in typeNum" :key="item.value" :label="item.label" :value="item.value">
							</el-option>
						</el-select>
					</el-form-item>
					<el-form-item label="教练昵称">
						<el-input class="order-in-service-jl-page__filter-input order-in-service-jl-page__filter-input--wide"
							@keydown.enter.native="select" placeholder="请输入教练昵称" v-model="artificerName">
						</el-input>
					</el-form-item>
					<el-form-item label="教练电话">
						<el-input class="order-in-service-jl-page__filter-input order-in-service-jl-page__filter-input--wide"
							@keydown.enter.native="select" placeholder="请输入教练电话" v-model="artificerPhone">
						</el-input>
					</el-form-item>
					<el-form-item label="用户昵称">
						<el-input class="order-in-service-jl-page__filter-input order-in-service-jl-page__filter-input--wide"
							@keydown.enter.native="select" placeholder="请输入用户昵称" v-model="userName">
						</el-input>
					</el-form-item>
					<el-form-item label="用户电话">
						<el-input class="order-in-service-jl-page__filter-input order-in-service-jl-page__filter-input--wide"
							@keydown.enter.native="select" placeholder="请输入用户电话" v-model="phone">
						</el-input>
					</el-form-item>
					<el-form-item label="是否超时">
						<el-select class="order-in-service-jl-page__filter-select" v-model="overTimeOrders" placeholder="请选择" @change="select()">
							<el-option v-for="item in statesnumCs" :key="item.value" :label="item.label" :value="item.value">
							</el-option>
						</el-select>
					</el-form-item>
					<el-form-item label="是否警告">
						<el-select class="order-in-service-jl-page__filter-select" v-model="warning" placeholder="请选择" @change="select()">
							<el-option v-for="item in statesnumCs" :key="item.value" :label="item.label" :value="item.value">
							</el-option>
						</el-select>
					</el-form-item>
					<el-form-item label="状态">
						<el-select class="order-in-service-jl-page__filter-select" v-model="status" placeholder="请选择状态" @change="animeDat(status)">
							<el-option v-for="item in statesnum" :key="item.value" :label="item.label" :value="item.value">
							</el-option>
						</el-select>
					</el-form-item>
					<el-form-item class="order-in-service-jl-page__filter-actions">
						<el-button type="primary" icon="el-icon-search" @click="select">查询</el-button>
						<el-button type="primary" icon="el-icon-refresh-left" @click="cleans">重置</el-button>
					</el-form-item>
				</el-form>
			</div>

			<div class="nf-table-card">
			<el-table v-loading="tableDataLoading" :data="tableData.list">
				<el-table-column fixed prop="id" label="编号" width="80">
					<template slot-scope="scope">
						<span>
							{{scope.$index+1}}
						</span>
					</template>
				</el-table-column>
				<el-table-column prop="userName" label="用户昵称" width="100">
					<template slot-scope="scope">
						<span class="order-in-service-jl-page__link" @click="updates(scope.row.userId)">
							{{ scope.row.userName?scope.row.userName:'未绑定'}}
						</span>
					</template>
				</el-table-column>
				<el-table-column prop="artificerName" label="教练昵称" width="100">
					<template slot-scope="scope">
						<span class="order-in-service-jl-page__link" @click="updates(scope.row.artificerId)">
							{{ scope.row.artificerName?scope.row.artificerName:'未绑定'}}
						</span>
					</template>
				</el-table-column>
				<el-table-column prop="ordersType" label="订单类型">
					<template slot-scope="scope">
						<span v-if="scope.row.ordersType==1">上门订单</span>
						<span v-if="scope.row.ordersType==2">到店订单</span>
					</template>
				</el-table-column>
				
				<el-table-column prop="phone" label="用户电话" width="160">
				</el-table-column>
				<el-table-column prop="artificerPhone" label="教练电话" width="160">
				</el-table-column>
				<el-table-column prop="address" label="服务地址"  width="200"></el-table-column>
				<el-table-column prop="longitude" label="用户经度" width="160">
				</el-table-column>
				<el-table-column prop="latitude" label="用户纬度" width="160">
				</el-table-column>
				<el-table-column prop="artificerAddress" label="教练地址"  width="200"></el-table-column>
				<el-table-column prop="artificerLongitude" label="教练经度" width="160">
				</el-table-column>
				<el-table-column prop="artificerLatitude" label="教练纬度" width="160">
				</el-table-column>
				<el-table-column prop="startTime" label="开始服务时间" width="100">
				</el-table-column>
				<el-table-column prop="endTime" label="预估结束时间" width="100">
				</el-table-column>
				<el-table-column prop="startAddress" label="开始服务地址" width="200">
				</el-table-column>
				<el-table-column prop="accomplishAddress" label="完成服务地址" width="200">
				</el-table-column>
				<el-table-column prop="startImg" label="服务开始图片" width="180">
					<template slot-scope="scope">
						<div style="display:flex;flex-wrap: wrap;" v-if="scope.row.startImg">
							<div v-for="(item,index) in scope.row.startImg.split(',')" :key="index">
								<el-popover placement="top-start" title="" trigger="hover">
									<img style="width: 50px; height: 50px;margin: 3px;" :src="item" alt=""
										slot="reference">
									<img style="width: 300px;height:auto" :src="item" alt="">
								</el-popover>
							</div>
						</div>
					</template>
				</el-table-column>
				<el-table-column prop="startLongitude" label="服务开始经度"></el-table-column>
				<el-table-column prop="startLatitude" label="服务开始纬度"></el-table-column>
				<el-table-column prop="accomplishLongitude" label="服务结束经度"></el-table-column>
				<el-table-column prop="accomplishLatitude" label="服务结束纬度"></el-table-column>
				<el-table-column prop="artificerStartTime" label="教练出发时间">
				</el-table-column>
				<el-table-column prop="artificerEndTime" label="教练到达时间">
				</el-table-column>
				<el-table-column prop="endTimes" label="订单完成时间" width="100">
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
				<el-table-column prop="warning" label="是否警告" fixed="right" width="80">
					<template slot-scope="scope">
						<span style="color: #f56c6c;" v-if="scope.row.warning==1">是</span>
						<span style="color: #999;" v-else>否</span>
					</template>
				</el-table-column>
				<el-table-column prop="overTimeOrders" label="是否超时" fixed="right" width="80">
					<template slot-scope="scope">
						<span style="color: #f56c6c;" v-if="scope.row.overTimeOrders==1">是</span>
						<span style="color: #999;" v-else>否</span>
					</template>
				</el-table-column>
				<el-table-column prop="status" label="订单状态" fixed="right">
					<template slot-scope="scope">
						<span style="color: #f56c6c;" v-if="scope.row.status==1">待支付</span>
						<span style="color: #E87D0D;" v-if="scope.row.status==2">待服务</span>
						<span style="color: #009900;" v-if="scope.row.status==3">待评价</span>
						<span style="color: #999;" v-if="scope.row.status==4">已取消</span>
						<span style="color: #999;" v-if="scope.row.status==5">已完成</span>
						<span style="color: red;" v-if="scope.row.status==6">服务中</span>
						<span style="color: #E87D0D;" v-if="scope.row.status==7">教练出发</span>
						<span style="color: #E87D0D;" v-if="scope.row.status==8">教练到达</span>
					</template>
				</el-table-column>
				<el-table-column prop="createAt" label="处理状态" width="120" fixed="right">
					<template slot-scope="scope">
						<el-button size="mini" type="danger" :disabled="!isAuth('orderCenterJl:wancheng')" v-if="scope.row.status==2||scope.row.status==6||scope.row.status==7||scope.row.status==8"
							@click="wancheng(scope.row)" style="margin:5px;" plain>完成订单</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div class="order-in-service-jl-page__pagination">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
				</el-pagination>
			</div>
			</div>
			
		</el-tab-pane>
		</el-tabs>
	</div>
</template>
<script>
	export default {
		data() {
			return {
				page: 1,
				limit: 10,
				classify: 1,
				indentNumber: '',
				imageUrl: '',
				url: '',
				complaintState: '',
				activeName: 'first',
				tableDataLoading: true,
				dialogFormVisible: false,
				dialogFormVisible2: false,
				tableData: [],
				checkBoxData: [], //多选框选择的值
				ordersType:'',
				typeNum: [{
						value: '',
						label: '全部'
					},
					{
						value: 1,
						label: '上门订单'
					},
					{
						value: 2,
						label: '到店订单'
					},
				],
				content: '',
				status: '',
				helpTakeId: '',
				indentNumbers: '',
				complaintId: '',
				message: '',
				title: '',
				type: 3,
				form: {
					id: '',
					illegal: '',
					forfeitMoney: '',
				},
				formLabelWidth: '200px',
				dialogFormVisible1: false,
				guangaoData: {},
				illegal: '',
				illegalId: '',
				forfeitMoney: '',
				illegalList: [],
				phone:'',
				artificerName:'',
				userName:'',
				artificerPhone:'',
				overTimeOrders:0,
				warning:0,
				statesnumCs: [{
						value: 0,
						label: '全部'
					},
					{
						value: 1,
						label: '是'
					},
					{
						value: 2,
						label: '否'
					}
				],
				statesnum: [{
						value: '',
						label: '全部'
					},
					{
						value: 2,
						label: '待服务'
					},
					{
						value: 5,
						label: '已完成'
					},
					{
						value: 6,
						label: '服务中'
					},
					{
						label: '教练出发',
						value: 7
					},
					{
						label: '教练到达',
						value: 8
					},
				],
			}
		},
		methods: {
			// 多选
			changeFun(val) {
				this.checkBoxData = val
			},
			// tabs切换
			handleClick(tab, event) {
				if (tab._props.label == '订单列表') {
					this.page = 1
					this.limit = 10
					this.dataSelect()
				}
				
			},
			handleSizeChange(val) {
				this.limit = val
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val
				this.dataSelect()
			},
			
			// 查询资源列表
			select() {
				this.page = 1
				this.limit = 10
				
				this.dataSelect()
			},
			// 重置资源列表
			cleans() {
				this.phone = ''
				this.artificerName = ''
				this.userName = ''
				this.artificerPhone = ''
				this.illegalId = ''
				this.overTimeOrders = 0
				this.warning = 0
				this.status = ''
				this.ordersType = ''
				this.page = 1
				this.dataSelect()
			},
			// select选择事件
			animeDat(state) {
				this.page = 1
				this.complaintState = state
				console.log(state)
				this.dataSelect()
			},
			animeDat1(state) {
				this.page = 1
				this.illegalId = state
				this.dataSelect()
			},

			
			// 获取数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('artificer/selectArtificerOrdersList'),
					method: 'get',
					params: this.$http.adornParams({
						'page':this.page,
						'limit':this.limit,
						'artificerName':this.artificerName,
						'phone':this.phone,
						'artificerPhone':this.artificerPhone,
						'userName':this.userName,
						'overTimeOrders':this.overTimeOrders,
						'status':'',
						'warning':this.warning,
						'ordersType':this.ordersType
						
					})
				}).then(({
					data
				}) => {
					if (data && data.code === 0) {
						this.tableDataLoading = false
						let returnData = data.data
						this.tableData = returnData
					}
				})
			},
			// 详情跳转
			updates(userId) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: userId
					}
				})
			},
			// 处理
			classdelete(row) {
				this.$confirm(`确定处理该投诉?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('message/auditMessage'),
						method: 'post',
						params: this.$http.adornParams({
							'ids': row.id,
							'status': 1,
							'auditContent': '',
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
				}).catch(() => {})
			},
			dataSelectD(){
				if(this.$route.query.types){
					if(this.$route.query.types==1){
						this.warning =1
						this.overTimeOrders =0
					}else{
						this.warning =0
						this.overTimeOrders =1
					}
				}else{
					this.warning = 0
					this.overTimeOrders = 0
				}
				this.dataSelect()
			},
			// 完成订单
			wancheng(row){
				let delid = row.ordersId
				this.$confirm(`确定进行完成订单操作吗?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('artificer/accomplishOrders?ordersId='+delid),
						method: 'post',
						params: this.$http.adornParams({})
					}).then(({
						data
					}) => {
						if(data.code==0){
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
								message: data.msg,
								type: 'warning',
								duration: 1500,
								onClose: () => {
								}
							})
						}
						
					})
				})
			},
		},
		mounted() {
			if(this.$route.query.types){
				if(this.$route.query.types==1){
					this.warning =1
					this.overTimeOrders =0
				}else{
					this.warning =0
					this.overTimeOrders =1
				}
			}else{
				this.warning = 0
				this.overTimeOrders = 0
			}
			this.dataSelect()
		},
		watch: {
			'$route': 'dataSelectD'
		}
	}
</script>

<style scoped>
.order-in-service-jl-page__filter-select {
	width: 150px;
}

.order-in-service-jl-page__filter-input {
	width: 150px;
}

.order-in-service-jl-page__filter-input--wide {
	width: 200px;
}

.order-in-service-jl-page__filter-actions .el-button + .el-button {
	margin-left: 12px;
}

.order-in-service-jl-page__tabs {
	margin-bottom: 0;
}

.order-in-service-jl-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.order-in-service-jl-page__tabs >>> .el-tabs__item.is-active {
	color: #5cc477;
}

.order-in-service-jl-page__tabs >>> .el-tabs__active-bar {
	background-color: #5cc477;
}

.order-in-service-jl-page__tabs .nf-filter-card {
	margin-top: 12px;
}

.order-in-service-jl-page__tabs .nf-table-card {
	margin-top: 12px;
	margin-bottom: 0;
}

.order-in-service-jl-page__link {
	color: #5cc477;
	cursor: pointer;
}

.order-in-service-jl-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}
</style>
