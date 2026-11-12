<template>
	<div class="nf-page travel-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">出行配置</span>
			<h2 class="nf-page-hero__title">出行配置</h2>
			<p class="nf-page-hero__desc">管理各城市出租车与公交出行计价规则，支持按城市筛选查询，可添加、修改与删除配置。</p>
			<div class="nf-page-hero__bar"></div>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">
				<i class="el-icon-search nf-filter-card__head-icon"></i>
				<span>筛选条件</span>
			</div>
			<el-form :inline="true" class="nf-filter-card__body" @submit.native.prevent>
				<el-form-item label="城市">
					<el-input
						class="travel-page__filter-input"
						@keydown.enter.native="Notice"
						clearable
						placeholder="请输入城市"
						v-model="cityT">
					</el-input>
				</el-form-item>
				<el-form-item class="travel-page__filter-actions">
					<el-button type="primary" icon="el-icon-search" @click="Notice">查询</el-button>
					<el-button type="primary" icon="el-icon-plus" :disabled="!isAuth('travel:add')" @click="addNotice">添加</el-button>
				</el-form-item>
			</el-form>
		</div>

		<div class="nf-table-card">
			<el-table v-loading="tableDataLoading" :data="tableData.list">
				<el-table-column fixed prop="tripId" label="编号" width="50">
				</el-table-column>
				<el-table-column prop="city" label="城市">
				</el-table-column>
				<el-table-column prop="taxiStartingFare" label="出租起步价">
				</el-table-column>
				<el-table-column prop="taxiStartingKm" label="出租起步公里">
				</el-table-column>
				<el-table-column prop="taxiKmMoney" label="出租每公里价格">
				</el-table-column>
				<el-table-column prop="busStartingFare" label="公交起步价">
				</el-table-column>
				<el-table-column prop="busStartingKm" label="公交起步公里">
				</el-table-column>
				<el-table-column prop="busKmMoney" label="公交每公里价格">
				</el-table-column>
				<el-table-column label="操作" width="180">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" :disabled="!isAuth('travel:update')"
							@click="amendBanner(scope.row)">修改
						</el-button>
						<el-button size="mini" type="danger" :disabled="!isAuth('travel:delete')"
							@click="deletes(scope.row)">删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div class="travel-page__pagination">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
				</el-pagination>
			</div>
		</div>

		<el-dialog :title="title" :visible.sync="dialogFormVisible" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">城市：</span>
				<el-input style="width:50%;" v-model="city" placeholder="请输入城市"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">出租起步价：</span>
				<el-input style="width:50%;" v-model="taxiStartingFare" type="number" min="0" placeholder="请输入出租起步价"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">出租起步公里：</span>
				<el-input style="width:50%;" v-model="taxiStartingKm" type="number" min="0" placeholder="请输入出租起步公里"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">出租每公里价格：</span>
				<el-input style="width:50%;" v-model="taxiKmMoney" type="number" min="0" placeholder="请输入出租每公里价格"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">公交起步价：</span>
				<el-input style="width:50%;" v-model="busStartingFare" type="number" min="0" placeholder="请输入公交起步价"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">公交起步公里：</span>
				<el-input style="width:50%;" v-model="busStartingKm" type="number" min="0" placeholder="请输入公交起步公里"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">公交每公里价格：</span>
				<el-input style="width:50%;" v-model="busKmMoney" type="number" min="0" placeholder="请输入公交每公里价格"></el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="addNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				page: 1,
				limit: 10,
				page1: 1,
				size1: 10,
				tableDataLoading: true,
				dialogFormVisible: false,
				tableData: [],
				tableDataY: {},
				city: '',
				busKmMoney: '',
				busStartingFare:'',
				busStartingKm:'',
				taxiStartingFare:'',
				taxiStartingKm:'',
				taxiKmMoney:'',
				tripId: '',
				giveNum: '',
				couponId: '',
				couponName: '',
				sort: '',
				payClassifyId: '',
				title: '添加',
				dialogFormVisible4: false,
				cityT:'',
			}
		},
		methods: {
			// 获取数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('trip/selectTripList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'city':this.cityT
					})
				}).then(({
					data
				}) => {
					if (data && data.code === 0) {
						console.log('会员列表', data)
						this.tableDataLoading = false
						let returnData = data.data;
						this.tableData = returnData;
					}

				})
			},
			Notice() {
				this.page = 1
				this.dataSelect()
			},
			addNotice() {
				this.city = ''
				this.tripId = ''
				this.busKmMoney = ''
				this.busStartingFare = ''
				this.busStartingKm = ''
				this.taxiStartingFare = ''
				this.taxiStartingKm = ''
				this.taxiKmMoney = ''
				this.title = '添加'
				this.dialogFormVisible = true
			},
			handleCurrentChange(val) {
				this.page = val
				this.dataSelect()
			},
			handleSizeChange(val) {
				this.limit = val
				this.dataSelect()
			},
			// 添加banner图
			addNoticeTo() {
				if (this.price == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入充值金额',
						type: 'warning'
					});
					return
				}
				if (this.title == '添加') {
					var url = 'trip/insertTrip'
				} else {
					var url = 'trip/updateTrip'
				}
				this.$http({
					url: this.$http.adornUrl(url),
					method: 'post',
					data: this.$http.adornData({
						'city': this.city,
						'tripId': this.tripId,
						'busKmMoney': this.busKmMoney,
						'busStartingFare': this.busStartingFare,
						'busStartingKm': this.busStartingKm,
						'taxiStartingFare': this.taxiStartingFare,
						'taxiStartingKm': this.taxiStartingKm,
						'taxiKmMoney': this.taxiKmMoney,
					})
				}).then(({
					data
				}) => {
					if(data.code==0){
						this.dialogFormVisible = false
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
			},
			// 修改
			amendBanner(rows) {
				this.city = rows.city
				this.tripId = rows.tripId
				this.busKmMoney = rows.busKmMoney
				this.busStartingFare = rows.busStartingFare
				this.busStartingKm = rows.busStartingKm
				this.taxiStartingFare = rows.taxiStartingFare
				this.taxiStartingKm = rows.taxiStartingKm
				this.taxiKmMoney = rows.taxiKmMoney
				this.title = '修改'
				this.dialogFormVisible = true
			},
			//删除一级
			deletes(row) {
				let delid = row.tripId
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`trip/deleteTripById/?tripId=${delid}`),
						method: 'post',
						params: this.$http.adornParams({})
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
				}).catch(() => {})
			},
		},
		mounted() {
			this.dataSelect()
		}
	}
</script>

<style scoped>
.travel-page__filter-input {
	width: 200px;
}

.travel-page__filter-actions .el-button + .el-button {
	margin-left: 12px;
}

.travel-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}

.nf-table-card .el-button + .el-button {
	margin-left: 8px;
}
</style>
