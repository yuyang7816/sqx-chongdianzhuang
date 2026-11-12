<template>
	<el-tabs v-model="activeName" @tab-click="handleClick">
		<el-tab-pane label="意见反馈" name="first">
			<el-table v-loading="tableDataLoading" :data="tableData.list">
				<el-table-column prop="id" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="title" label="联系方式">
				</el-table-column>
				<el-table-column prop="content" label="内容">
				</el-table-column>
				<el-table-column prop="createAt" label="创建时间" width="180">
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
				</el-pagination>
			</div>
		</el-tab-pane>
		<el-tab-pane label="注销账号" name="fourth">
			<el-table v-loading="tableDataLoading" :data="tableData.list">
				<el-table-column prop="id" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="title" label="联系方式">
				</el-table-column>
				<el-table-column prop="content" label="内容">
				</el-table-column>
				<el-table-column prop="createAt" label="创建时间" width="180">
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
				</el-pagination>
			</div>
		</el-tab-pane>
		<!-- <el-tab-pane label="城市投票" name="five">
			<el-table v-loading="tableDataLoading" :data="tableData.list">
				<el-table-column prop="id" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="userName" label="用户名称">
				</el-table-column>
				<el-table-column prop="title" label="消息标题">
				</el-table-column>
				<el-table-column prop="content" label="消息内容">
				</el-table-column>
				<el-table-column prop="createAt" label="创建时间">
				</el-table-column>
				<el-table-column label="操作">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" @click="updataDetails(scope.row)">用户详情
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
				</el-pagination>
			</div>
		</el-tab-pane> -->
		<el-tab-pane label="消息推送" name="seventh">
			<div style="float: right;margin-right:2%;">
				<el-button style='margin: 10px 0;' :disabled="!isAuth('message:push')" size="mini"  type="primary" icon="document" @click="magNotice">消息推送</el-button>
			</div>
			<el-table
			v-loading="tableDataLoading"
				:data="tableData.list">
			<el-table-column
				fixed
			 prop="id"
			 label="编号"
			 width="80">
			 </el-table-column>
			 <el-table-column
				prop="userName"
				label="用户名称">
				</el-table-column>
				<el-table-column
				 prop="title"
				 label="消息标题">
				 </el-table-column>
				<el-table-column
				prop="content"
				label="消息内容"
				width="280">
				</el-table-column>
				<el-table-column
				prop="createAt"
				label="创建时间">
				</el-table-column>
				<el-table-column
					label="操作"
				width="120">
					<template slot-scope="scope">
						<el-button
							size="mini"
							type="primary"
							@click="updataDetails(scope.row)">用户详情
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination
					@size-change="handleSizeChange"
					@current-change="handleCurrentChange"
					:page-sizes="[5, 10, 15, 20]"
					:page-size="limit"
					:current-page="page"
					layout="total,sizes, prev, pager, next,jumper"
					:total="tableData.totalCount">
				</el-pagination>
			</div>
		<!-- 消息推送 -->
		<el-dialog title="消息推送" :visible.sync="dialogFormVisible2" center>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">选择：</span>
					<el-select v-model="flag" placeholder="请选择类型" style="width:50%" >
					  <el-option v-for="item in flags" :key="item.value" :label="item.label" :value="item.value">
					  </el-option>
					</el-select>&nbsp;&nbsp;&nbsp;&nbsp;
				</div>
				<div style="margin-bottom: 10px;" v-if="flag == 1">
					<span style="width: 200px;display: inline-block;text-align: right;">用户手机号：</span>
					<el-input style="width: 50%;" v-model="phone" placeholder="请输入用户手机号"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">消息标题：</span>
					<el-input style="width: 50%;" v-model="title" placeholder="请输入消息标题"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;position: relative;top: -65px;">消息内容：</span>
					<el-input style="width: 50%;" type="textarea" rows="4" v-model="content" placeholder="请输入消息内容"></el-input>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible2 = false">取 消</el-button>
					<el-button type="primary" @click="magNoticeTo()">确 定</el-button>
				</div>
			</el-dialog>
		</el-tab-pane>
	</el-tabs>
</template>

<script>
	export default {
		data() {
			return {
				limit: 10,
				page: 1,
				state: 2,
				url: '',
				title: '',
				type: '',
				id: '',
				types: [{
						value: 'word',
						label: '文字'
					},
					{
						value: 'url',
						label: '链接'
					}
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
			}
		},
		methods: {
			// 详情跳转
			updataDetails(row) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: row.userId
					}
				});
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
				if (tab._props.label == '意见反馈') {
					this.page = 1
					this.limit = 10
					this.state = 2
					this.dataSelect()
				}
				if (tab._props.label == '注销账号') {
					this.page = 1
					this.limit = 10
					this.state = 3
					this.dataSelect()
				}
				if (tab._props.label == '消息推送') {
					this.page = 1
					this.limit = 10
					this.state = 8
					this.dataSelect()
				}
			},
			// 消息推送
			magNotice() {
				this.dialogFormVisible2 = true
			},
			// 消息推送
			magNoticeTo() {
				if (this.flag == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择类型',
						type: 'warning'
					});
					return
				}
				if (this.title == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入标题',
						type: 'warning'
					});
					return
				}
				if (this.content == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入内容',
						type: 'warning'
					});
					return
				}
				if (this.flag == 1) {
					if (this.phone == '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请输入用户手机号',
							type: 'warning'
						});
						return
					}
					this.$http({
						url: this.$http.adornUrl('/cash/sendMsg'),
						method: 'get',
						params: this.$http.adornParams({
							'title': this.title,
							'content': this.content,
							'phone': this.phone,
							'flag': this.flag
						})
					}).then(({
						data
					}) => {
						this.dialogFormVisible2 = false
						if (data.code == -100) {
							this.$message({
								message: data.msg,
								type: 'error',
								duration: 1500,
								onClose: () => {
									this.title = ''
									this.content = ''
									this.phone = ''
									this.flag = ''
									this.dataSelect()
								}
							})
						}
						if (data.code == 0) {
							this.$message({
								message: '操作成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.title = ''
									this.content = ''
									this.phone = ''
									this.flag = ''
									this.dataSelect()
								}
							})
						}
					})
				}
				if (this.flag == 2) {
					this.$http({
						url: this.$http.adornUrl('/cash/sendMsg'),
						method: 'get',
						params: this.$http.adornParams({
							'title': this.title,
							'content': this.content,
							'flag': this.flag,
						})
					}).then(({
						data
					}) => {
						this.dialogFormVisible2 = false
						if (data.code == 0) {
							this.$message({
								message: '操作成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.title = ''
									this.content = ''
									this.flag = ''
									this.dataSelect()
								}
							})
						}
					})
				}
			},
			// 获取数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl(`message/page/${this.state}/${this.page}/${this.limit}`),
					method: 'get',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					// for (var i in data.data.list) {
					// 	var a = data.data.list[i].content.toString()
					// 	var b = JSON.parse(a)
					// 	data.data.list[i].content = b.content
					// 	console.log('key', b.content)
					// }
					let returnData = data.data;
					this.tableData = returnData

				})
			},
		},
		mounted() {
			this.dataSelect()
		}
	};
</script>

<style>

</style>
