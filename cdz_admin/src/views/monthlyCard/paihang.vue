<template>
	<div class="nf-page paihang-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">数据统计</span>
			<h2 class="nf-page-hero__title">开台排行</h2>
			<p class="nf-page-hero__desc">查看用户开台时长与订单数排行，支持按全国、城市、店铺维度及时间范围筛选查询。</p>
			<div class="nf-page-hero__bar"></div>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">
				<i class="el-icon-search nf-filter-card__head-icon"></i>
				<span>筛选条件</span>
			</div>
			<el-form :inline="true" class="nf-filter-card__body" @submit.native.prevent>
				<el-form-item label="用户昵称">
					<el-input
						class="paihang-page__filter-input"
						@keydown.enter.native="select"
						clearable
						placeholder="请输入用户昵称"
						v-model="title">
					</el-input>
				</el-form-item>
				<el-form-item label="类型">
					<el-select
						class="paihang-page__filter-select"
						v-model="scoreType"
						placeholder="请选择类型"
						@change="select()">
						<el-option v-for="item in scoreTypes" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="城市" v-if="scoreType==2">
					<el-input
						class="paihang-page__filter-input"
						@keydown.enter.native="select"
						clearable
						placeholder="请输入城市"
						v-model="city">
					</el-input>
				</el-form-item>
				<el-form-item label="台球馆名称" v-if="scoreType==3">
					<el-select
						class="paihang-page__filter-select--wide"
						v-model="tearoomName"
						placeholder="请选择台球馆"
						@change="select()"
						filterable>
						<el-option v-for="item in tableDataTqt" :key="item.tearoomId" :label="item.tearoomName" :value="item.tearoomName">
						</el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="开始时间">
					<el-date-picker
						class="paihang-page__filter-date"
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
						class="paihang-page__filter-date"
						v-model="endTime"
						align="right"
						type="datetime"
						format="yyyy-MM-dd"
						value-format="yyyy-MM-dd"
						placeholder="选择截止时间">
					</el-date-picker>
				</el-form-item>
				<el-form-item class="paihang-page__filter-actions">
					<el-button type="primary" icon="el-icon-search" @click="select">查询</el-button>
					<el-button type="primary" icon="el-icon-refresh-left" @click="cleans">重置</el-button>
				</el-form-item>
			</el-form>
		</div>

		<div class="nf-table-card">
			<el-table v-loading="tableDataLoading" :data="tableData.records">
				<el-table-column prop="userId" label="编号" width="80">
					<template slot-scope="scope">
						{{scope.$index+1}}
					</template>
				</el-table-column>
				<el-table-column prop="userName" label="用户昵称">
					<template slot-scope="scope">
						<span class="paihang-page__link"
							@click="updates(scope.row.userId)">{{scope.row.userName ? scope.row.userName : '未绑定'}}</span>
					</template>
				</el-table-column>
				<el-table-column label="图像">
					<template slot-scope="scope">
						<img v-if="scope.row.avatar==null" src="~@/assets/img/avatar.png" alt="" width="40"
							height="40">
						<img v-else :src="scope.row.avatar" alt="" width="40" height="40">
					</template>
				</el-table-column>
				<el-table-column prop="ordersCount" label="订单总数">
				</el-table-column>
				<el-table-column prop="durationHms" label="开台总时长">
				</el-table-column>
			</el-table>
			<div class="paihang-page__pagination">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
					layout="total,sizes, prev, pager, next" :total="tableData.total">
				</el-pagination>
			</div>
		</div>

		<!-- 回复 -->
		<el-dialog title="回复" :visible.sync="dialogFormVisible" center>
			<el-form :model="form">
				<el-form-item label="回复内容：" :label-width="formLabelWidth">
					<el-input v-model="form.reply" type="textarea" :rows="4" style="width:65%;" placeholder="请输入回复内容">
					</el-input>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="replyNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				size: 10,
				page: 1,
				goodsId: '',
				form: {
					commentId: '',
					reply: ''
				},
				scoreType: '',
				scoreTypes: [
					{
						value: '',
						label: '全国'
					},
					{
						value: 2,
						label: '城市'
					},
					{
						value: 3,
						label: '店铺'
					}
				],
				formLabelWidth: '200px',
				tableDataLoading: false,
				dialogFormVisible: false,
				tableData: [],
				content: '',
				title: '',
				userName: '',
				phone: '',
				startTime: '',
				endTime: '',
				tearoomName:'',
				city:'',
				tableDataTqt:[],
			}
		},
		methods: {
			// 返回上一级
			prev() {
				this.$router.back()
			},
			handleSizeChange(val) {
				this.size = val;
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val;
				this.dataSelect()
			},
			// 刷新
			refresh() {
				this.dataSelect()
			},
			//搜索
			select() {
				if(this.scoreType==''){
					this.tearoomName = ''
					this.city = ''
				}
				this.page = 1
				this.dataSelect()
			},
			// 重置
			cleans() {
				this.title = ''
				this.userName = ''
				this.endTime = ''
				this.startTime = ''
				this.tearoomName = ''
				this.city = ''
				this.scoreType = ''
				this.page = 1
				this.dataSelect()
				this.dataSelectTqt()
			},
			// 回复
			replyClick(row) {
				this.dialogFormVisible = true
				this.form.commentId = row.commentId
				this.form.reply = row.reply
			},
			replyNoticeTo() {
				this.$http({
					url: this.$http.adornUrl('selfGoodsComment/reply'),
					method: 'get',
					params: this.$http.adornParams({
						'commentId': this.form.commentId,
						'reply': this.form.reply,
					})
				}).then(({
					data
				}) => {
					this.$message({
						message: '回复成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.dataSelect()
						}
					})
					this.dialogFormVisible = false
				})
			},
			// 删除评论
			deletes(row) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`selfGoodsComment/delete?id=${row.commentId}`),
						method: 'get',
						params: this.$http.adornParams({})
					}).then(({
						data
					}) => {
						if (data.status == 0) {
							this.$message({
								message: '删除成功',
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
			// 获取数据
			dataSelect() {
				let goodsId = this.$route.query.goodsId
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('orders/selectUserByOrdersHourList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.size,
						'userName': this.title,
						'phone':'',
						'city':this.city,
						'startTime':this.startTime,
						'endTime':this.endTime,
						'tearoomName':this.tearoomName,

					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					if(data.data.records.length>0){
						for(var i in data.data.records){
							if(data.data.records[i].duration){
								const seconds = Number(data.data.records[i].duration)
								const hours = Math.floor(seconds / 3600); // 10 / 3600 约等于 0.0027，Math.floor 取整后为 0
								    // 计算剩余的秒数
								    seconds = seconds % 3600; // 10 % 3600 结果为 10
								    // 计算分钟数
								    const minutes = Math.floor(seconds / 60); // 10 / 60 约等于 0.167，Math.floor 取整后为 0
								    // 计算最终剩余的秒数
								    seconds = seconds % 60; // 10 % 60 结果为 10
								    // 将结果格式化为字符串，确保每个部分都是两位数
								    const formattedHours = String(hours).padStart(2, '0'); // 结果为 "00"
								    const formattedMinutes = String(minutes).padStart(2, '0'); // 结果为 "00"
								    const formattedSeconds = String(seconds).padStart(2, '0'); // 结果为 "10"
								
								data.data.records[i].durationHms = formattedHours+'时'+formattedMinutes+'分'+formattedSeconds+'秒'
							}
						}
					}
					
					let returnData = data.data;
					this.tableData = returnData
				})
			},
			// 详情跳转
			updates(userId) {
				if(userId){
					this.$router.push({
						path: '/userDetail',
						query: {
							userId: userId
						}
					})
				}
				
			},
			dataSelectTqt() {
				this.$http({
					url: this.$http.adornUrl('tearoom/selectTearoomList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': -1,
						'limit': 50,
						'tearoomName': '',
						'userName': '',
						'city': '',
						'status': '',
			
					})
				}).then(({
					data
				}) => {
					let returnData = data.data
					this.tableDataTqt = returnData.list
				})
			},
		},
		activated() {
			this.dataSelect()
			this.dataSelectTqt()
		}
	};
</script>

<style scoped>
.paihang-page__filter-input {
	width: 150px;
}

.paihang-page__filter-select {
	width: 150px;
}

.paihang-page__filter-select--wide {
	width: 200px;
}

.paihang-page__filter-date {
	width: 160px;
}

.paihang-page__filter-actions .el-button + .el-button {
	margin-left: 12px;
}

.paihang-page__link {
	color: #5cc477;
	cursor: pointer;
}

.paihang-page__link:hover {
	color: #3fae5c;
}

.paihang-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}
</style>
