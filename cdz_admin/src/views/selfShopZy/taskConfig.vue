<template>
	<div class="nf-page task-config-zy-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">自营商城</span>
			<h2 class="nf-page-hero__title">商城配置</h2>
			<p class="nf-page-hero__desc">管理会员抽成比率、直属与非直属分成等商城任务配置，支持按会员类别查看与修改金额。</p>
			<div class="nf-page-hero__bar"></div>
		</div>

		<el-tabs v-model="activeName" @tab-click="handleClick" class="task-config-zy-page__tabs">
		<el-tab-pane label="会员配置" name="first">
			<div class="nf-table-card">
			<el-table
			v-loading="tableDataLoading"
			  :data="tableData"
			  height="450px">
			<el-table-column
			  fixed
			prop="id"
			  label="编号">
			</el-table-column>
			<el-table-column
			  prop="rate"
			  label="抽成比率或价格">
			</el-table-column>
			<el-table-column
			  prop="type"
			  label="类别">
					<template slot-scope="scope">
					   <span v-if='scope.row.type == 1'>非会员</span>
					   <span v-if='scope.row.type == 2'>初级会员</span>
					   <span v-if='scope.row.type == 3'>中级会员</span>
					   <span v-if='scope.row.type == 4'>高级会员</span>
					</template>
			</el-table-column>
			<el-table-column
				label="操作"
				width="100">
				<template slot-scope="scope">
				  <el-button
					size="mini"
					type="primary"
					:disabled="!isAuth('taskConfig:update')"
					@click="amend(scope.$index, scope.row)">修改
				  </el-button>
				</template>
			  </el-table-column>
			</el-table>
			</div>
		</el-tab-pane>
		<!-- <el-tab-pane label="会员开通" name="second">
			<div class="nf-table-card">
			<el-table
			v-loading="tableDataLoading"
			  :data="tableData"
			  height="450px">
			<el-table-column
			  fixed
			prop="id"
			  label="编号">
			</el-table-column>
			<el-table-column
			  prop="rate"
			  label="抽成比率或价格">
			</el-table-column>
			<el-table-column
			  prop="type"
			  label="类别">
					<template slot-scope="scope">
					   <span v-if='scope.row.type == 1'>非会员</span>
					   <span v-if='scope.row.type == 2'>初级会员</span>
					   <span v-if='scope.row.type == 3'>中级会员</span>
					   <span v-if='scope.row.type == 4'>高级会员</span>
					</template>
			</el-table-column>
			<el-table-column
				label="操作"
				width="100">
				<template slot-scope="scope">
				  <el-button
					size="mini"
					type="primary"
					:disabled="!isAuth('taskConfig:update')"
					@click="amend(scope.$index, scope.row)">修改
				  </el-button>
				</template>
			  </el-table-column>
			</el-table>
			</div>
		</el-tab-pane> -->
		<el-tab-pane label="直属分成" name="third">
			<div class="nf-table-card">
			<el-table
			v-loading="tableDataLoading"
			  :data="tableData"
			  height="450px">
			<el-table-column
			  fixed
			prop="id"
			  label="编号">
			</el-table-column>
			<el-table-column
			  prop="rate"
			  label="抽成比率或价格">
			</el-table-column>
			<el-table-column
			  prop="type"
			  label="类别">
					<template slot-scope="scope">
					   <span v-if='scope.row.type == 1'>非会员</span>
					   <span v-if='scope.row.type == 2'>初级会员</span>
					   <span v-if='scope.row.type == 3'>中级会员</span>
					   <span v-if='scope.row.type == 4'>高级会员</span>
					</template>
			</el-table-column>
			<el-table-column
				label="操作"
				width="100">
				<template slot-scope="scope">
				  <el-button
					size="mini"
					type="primary"
					:disabled="!isAuth('taskConfig:update')"
					@click="amend(scope.$index, scope.row)">修改
				  </el-button>
				</template>
			  </el-table-column>
			</el-table>
			</div>
		</el-tab-pane>
		<el-tab-pane label="非直属分成" name="fifth">
			<div class="nf-table-card">
			<el-table
			v-loading="tableDataLoading"
			  :data="tableData"
			  height="450px">
			<el-table-column
			  fixed
			prop="id"
			  label="编号">
			</el-table-column>
			<el-table-column
			  prop="rate"
			  label="抽成比率或价格">
			</el-table-column>
			<el-table-column
			  prop="type"
			  label="类别">
					<template slot-scope="scope">
					   <span v-if='scope.row.type == 1'>非会员</span>
					   <span v-if='scope.row.type == 2'>初级会员</span>
					   <span v-if='scope.row.type == 3'>中级会员</span>
					   <span v-if='scope.row.type == 4'>高级会员</span>
					</template>
			</el-table-column>
			<el-table-column
				label="操作"
				width="100">
				<template slot-scope="scope">
				  <el-button
					size="mini"
					type="primary"
					:disabled="!isAuth('taskConfig:update')"
					@click="amend(scope.$index, scope.row)">修改
				  </el-button>
				</template>
			  </el-table-column>
			</el-table>
			</div>
		</el-tab-pane>
		<!-- <el-tab-pane label="邀请人数" name="fourth">
			<div class="nf-table-card">
			<el-table
				v-loading="tableDataLoading"
				  :data="tableData"
				  height="450px">
				<el-table-column
				  fixed
				prop="id"
				  label="编号">
				</el-table-column>
				<el-table-column
				  prop="rate"
				  label="抽成比率或价格">
				</el-table-column>
				<el-table-column
				  prop="type"
				  label="类别">
				<template slot-scope="scope">
				   <span v-if='scope.row.type == 1'>非会员</span>
				   <span v-if='scope.row.type == 2'>初级会员</span>
				   <span v-if='scope.row.type == 3'>中级会员</span>
				   <span v-if='scope.row.type == 4'>高级会员</span>
				</template>
				</el-table-column>
				<el-table-column
					label="操作"
					width="100">
					<template slot-scope="scope">
					  <el-button
						size="mini"
						type="primary"
						:disabled="!isAuth('taskConfig:update')"
						@click="amend(scope.$index, scope.row)">修改
					  </el-button>
					</template>
				  </el-table-column>
			</el-table>
			</div>
		</el-tab-pane>
		<el-tab-pane label="任务配置" name="renwu">
			<div class="nf-table-card">
			<el-table
				v-loading="tableDataLoading"
				:data="renwuData">
				<el-table-column
				  fixed
					prop="type"
				  label="编号"
				  align="center"
				  width="80">
				</el-table-column>
				<el-table-column
				  prop="min"
				  label="类型">
				</el-table-column>
				<el-table-column
				  prop="value"
				  label="内容"
				  width="500">
				</el-table-column>
				<el-table-column
					prop="createAt"
					label="创建时间">
				</el-table-column>
				<el-table-column
					label="操作"
					prop="id"
					width="120">
					<template slot-scope="scope">
						<el-button
						  size="mini"
						  type="primary"
							:disabled="!isAuth('allocationList:update')"
						  @click="renwuAmend(scope.$index, scope.row)">编辑
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			</div>
		</el-tab-pane> -->
		</el-tabs>
		<!-- 修改 -->
		<el-dialog title="修改" :visible.sync="dialogFormVisible" center>
			<el-form :model="form">
			  <el-form-item label="金额：" :label-width="formLabelWidth" >
			    <el-input v-model="form.rate" style="width:65%;" ></el-input>
			  </el-form-item>
			  <!-- <el-form-item label="名称：" :label-width="formLabelWidth" >
			    <el-input v-model="form.name" style="width:65%;" ></el-input>
			  </el-form-item> -->
			</el-form>
		  <div slot="footer" class="dialog-footer">
		    <el-button @click="dialogFormVisible = false">取 消</el-button>
		    <el-button type="primary" @click="amendNoticeTo()">确 定</el-button>
		  </div>
		</el-dialog>
		<!-- 修改任务弹框 -->
		<el-dialog title="修改" :visible.sync="dialogFormVisible1" center>
			<el-form :model="form1">
			  <el-form-item label="配置类型：" :label-width="formLabelWidth" >
				<el-input v-model="form1.min" style="width:65%;" readonly ></el-input>
			  </el-form-item>
			  <el-form-item label="内容：" :label-width="formLabelWidth" >
				<el-input v-model="form1.value" style="width:65%;"></el-input>
			  </el-form-item>
			</el-form>
		  <div slot="footer" class="dialog-footer">
			<el-button @click="dialogFormVisible1 = false">取 消</el-button>
			<el-button type="primary" @click="renwuNoticeTo()">确 定</el-button>
		  </div>
		</el-dialog>
	</div>
</template>

<script>
    export default {
        data() {
            return {
				page:1,
				limit:10,
				state:1,
				openValue:1,
				closeValue:2,
				classify:1,
				money:'',
				condition:'',
				activeName: 'first',
				dialogFormVisible:false,
                tableDataLoading: true,
				dialogFormVisible1:false,
				formLabelWidth:'200px',
                tableData: [],
				renwuData:[],
				form:{
					id:'',
					rate:''
				},
				form1:{
					id:'',
					min:'',
					value:'',
					type:'',
				},
            }
        },
        methods: {
			// tabs切换
			handleClick(tab, event) {
				if(tab._props.label=='会员配置'){
					this.classify = 1
					this.dataSelect()
				}
				if(tab._props.label=='会员开通'){
					this.classify = 2
					this.dataSelect()
				}
				if(tab._props.label=='直属分成'){
					this.classify = 5
					this.dataSelect()
				}
				if(tab._props.label=='非直属分成'){
					this.classify = 6
					this.dataSelect()
				}
				if(tab._props.label=='邀请人数'){
					this.classify = 4
					this.dataSelect()
				}
				if(tab._props.label=='任务配置'){
					this.condition = 'renwu'
					this.renwuSelect()
				}
			},
            handleSizeChange(val) {
                this.limit = val;
                this.dataSelect();
            },
            handleCurrentChange(val) {
                this.page = val;
                this.dataSelect();
            },
			// 修改
			amend(index,rows){
				this.dialogFormVisible = true
				this.form.id = rows.id
				this.form.rate = rows.rate
			},
			// 修改事件
			amendNoticeTo(){
				this.$http({
				  url: this.$http.adornUrl(`cash/updateByMoney/${this.form.id}/${this.form.rate}`),
				  method: 'post',
				  data: this.$http.adornData({
				  })
				}).then(({data}) => {
					this.dialogFormVisible = false
					this.$message({
					  message: '操作成功',
					  type: 'success',
					  duration: 1500,
					  onClose: () => {
						this.form.rate = ''
					    this.dataSelect()
					  }
					})
				})
			},
			// 修改任务弹框
			renwuAmend(index,rows){
				this.dialogFormVisible1=true;
				this.form1.id = rows.id;
				this.form1.type = rows.type
				this.form1.min = rows.min;
				this.form1.value = rows.value;
				this.form1.max = rows.max;
				this.form1.createAt = rows.createAt;
				this.form1.conditionFrom = rows.conditionFrom
			},
			// 修改任务
			renwuNoticeTo(){
				this.$http({
				  url: this.$http.adornUrl('common/update'),
				  method: 'post',
				  data: this.$http.adornData({
					'id':this.form1.id,
				   'type': this.form1.type,
					'value': this.form1.value,
					'min':this.form1.min,
					'max': this.form1.max,
					'createAt':this.form1.createAt,
					'conditionFrom':this.form1.conditionFrom
				  })
				}).then(({data}) => {
				  if (data && data.code === 0) {
					this.dialogFormVisible1 = false
				    this.$message({
				      message: '操作成功',
				      type: 'success',
				      duration: 1500,
				      onClose: () => {
				        this.renwuSelect()
				      }
				    })
				  } else {
				    this.$message.error(data.msg)
				  }
				})
			},
			// 获取数据列表
			dataSelect () {
			  this.tableDataLoading = true
			  this.$http({
			    url: this.$http.adornUrl('cash/selectRateByRateList'),
			    method: 'get',
			    params: this.$http.adornParams({
			      'classify': this.classify,
            'isShopTask':2
			    })
			  }).then(({data}) => {
				  if (data && data.code === 0) {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableData = returnData;
				  }
			  })
			},
			renwuSelect(){
				this.tableDataLoading = true
				this.$http({
				  url: this.$http.adornUrl(`common/type/condition/${this.condition}`),
				  method: 'get',
				  params: this.$http.adornParams({
				  })
				}).then(({data}) => {
				  if (data && data.code === 0) {
					this.tableDataLoading = false
					let returnData = data.data;
					this.renwuData = returnData
				  }
				})
			}
        },
        mounted() {
			 this.dataSelect()
        }
    }
</script>

<style scoped>
.task-config-zy-page__tabs {
	margin-bottom: 0;
}

.task-config-zy-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.task-config-zy-page__tabs >>> .el-tabs__item.is-active {
	color: #5cc477;
}

.task-config-zy-page__tabs >>> .el-tabs__active-bar {
	background-color: #5cc477;
}

.task-config-zy-page__tabs .nf-table-card {
	margin-top: 12px;
	margin-bottom: 0;
}
</style>
