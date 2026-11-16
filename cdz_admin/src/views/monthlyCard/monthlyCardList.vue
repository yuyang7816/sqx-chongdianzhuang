<template>
	<div class="nf-page monthly-card-list-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">卡券管理</span>
			<h2 class="nf-page-hero__title">卡券赠送列表</h2>
			<p class="nf-page-hero__desc">查看卡券赠送记录，支持按卡券名称、赠送用户及电话筛选查询，可跳转用户详情。</p>
			<div class="nf-page-hero__bar"></div>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">
				<i class="el-icon-search nf-filter-card__head-icon"></i>
				<span>筛选条件</span>
			</div>
			<el-form :inline="true" class="nf-filter-card__body" @submit.native.prevent>
				<el-form-item label="卡券名称">
					<el-input
						class="monthly-card-list-page__filter-input"
						@keydown.enter.native="select"
						clearable
						placeholder="请输入卡券名称"
						v-model="couponName">
					</el-input>
				</el-form-item>
				<el-form-item label="赠送用户电话">
					<el-input
						class="monthly-card-list-page__filter-input"
						@keydown.enter.native="select"
						clearable
						placeholder="请输入赠送用户电话"
						v-model="phone">
					</el-input>
				</el-form-item>
				<el-form-item label="赠送用户">
					<el-input
						class="monthly-card-list-page__filter-input"
						@keydown.enter.native="select"
						clearable
						placeholder="请输入赠送用户"
						v-model="userName">
					</el-input>
				</el-form-item>
				<!-- <el-form-item label="状态">
					<el-select v-model="status" class="monthly-card-list-page__filter-select" @change="select(status)">
						<el-option v-for="item in statesnum" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</el-form-item> -->
				<el-form-item class="monthly-card-list-page__filter-actions">
					<el-button type="primary" icon="el-icon-search" @click="select">查询</el-button>
					<el-button type="primary" icon="el-icon-refresh-left" @click="cleans">重置</el-button>
					<el-button type="primary" icon="el-icon-refresh" @click="refresh">刷新</el-button>
				</el-form-item>
			</el-form>
		</div>

		<div class="nf-table-card">
			<el-table v-loading="tableDataLoading" :data="tableData.records">
				<el-table-column prop="recordId" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="cardName" label="卡券名称" width="150">
				</el-table-column>
				<el-table-column prop="sysUserName" label="操作用户">
				</el-table-column>
				<el-table-column prop="userName" label="赠送用户">
					<template slot-scope="scope">
						<span class="monthly-card-list-page__link"
							@click="updates(scope.row)"
							v-if="scope.row.userName">{{ scope.row.userName }}</span>
					</template>
				</el-table-column>
				<el-table-column prop="userPhone" label="赠送用户电话">
				</el-table-column>
				<!-- <el-table-column prop="avatar" label="用户头像">
					<template slot-scope="scope">
						<img v-if="scope.row.avatar" :src="scope.row.avatar" alt="" width="40" height="40">
					</template>
				</el-table-column> -->
				<el-table-column prop="num" label="赠送数量">
				</el-table-column>
				<!-- <el-table-column label="状态">
					<template slot-scope="scope">
						<span class="monthly-card-list-page__status-on" v-if="scope.row.status == 0">未使用</span>
						<span v-if="scope.row.status == 1">已使用</span>
						<span v-if="scope.row.status == 2">已过期</span>
					</template>
				</el-table-column> -->
				<el-table-column prop="createTime" label="创建时间" width="150">
				</el-table-column>
			</el-table>
			<div class="monthly-card-list-page__pagination">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
					layout="total,sizes, prev, pager, next" :total="tableData.total">
				</el-pagination>
			</div>
		</div>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				size: 10,
				page: 1,
				couponName: '',
				formLabelWidth: '200px',
				tableDataLoading: false,
				dialogFormVisible: false,
				tableData: [],
				phone: '',
				status: '',
				statesnum: [{
						label: '全部',
						value: ''
					},
					{
						label: '未使用',
						value: 0
					},
					{
						label: '已使用',
						value: 1
					},
					{
						label: '已过期',
						value: 2
					},
				],
				userName: '',
			}
		},

		methods: {
			handleSizeChange(val) {
				this.size = val;
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val;
				this.dataSelect()
			},
			// 查询
			select() {
				this.page = 1
				this.dataSelect()
			},
			// 重置
			cleans() {
				this.page = 1
				this.phone = ''
				this.userName = ''
				this.couponName = ''
				this.dataSelect()
			},
			// 刷新
			refresh() {
				this.dataSelect()
			},
			//获取已发布优惠券数据
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('admin/giveCardRecord/getCardRecordList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.size,
						'cardName': this.couponName,
						'userPhone': this.phone,
						'userName': this.userName,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableData = returnData
				})
			},
			// 详情跳转
			updates(row) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: row.userId
					}
				})
			},
		},
		mounted() {
			this.dataSelect()
		}
	};
</script>

<style scoped>
.monthly-card-list-page__filter-input {
	width: 200px;
}

.monthly-card-list-page__filter-select {
	width: 150px;
}

.monthly-card-list-page__filter-actions .el-button + .el-button {
	margin-left: 12px;
}

.monthly-card-list-page__status-on {
	color: #5cc477;
}

.monthly-card-list-page__link {
	color: #5cc477;
	cursor: pointer;
}

.monthly-card-list-page__link:hover {
	color: #3fae5c;
}

.monthly-card-list-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}
</style>
