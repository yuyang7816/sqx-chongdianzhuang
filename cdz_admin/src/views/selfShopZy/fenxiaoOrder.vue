<template>
  <div class="nf-page fenxiao-order-zy-page">
    <div class="nf-page-hero">
      <span class="nf-page-hero__badge">自营商城</span>
      <h2 class="nf-page-hero__title">分销订单</h2>
      <p class="nf-page-hero__desc">查看分销佣金订单记录，支持按分销状态、手机号筛选查询，可跳转查看用户详情。</p>
      <div class="nf-page-hero__bar"></div>
    </div>

    <div class="nf-filter-card">
      <div class="nf-filter-card__head">
        <i class="el-icon-search nf-filter-card__head-icon"></i>
        <span>筛选条件</span>
      </div>
      <el-form :inline="true" class="nf-filter-card__body" @submit.native.prevent>
        <el-form-item label="分销状态">
          <el-select
            class="fenxiao-order-zy-page__filter-select"
            v-model="status"
            placeholder="请选择分销类型"
            @change="selectTrigger(status)">
            <el-option v-for="item in statusnum" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="手机号">
          <el-input
            class="fenxiao-order-zy-page__filter-input"
            @keydown.enter.native="select"
            clearable
            placeholder="请输入手机号"
            v-model="phone">
          </el-input>
        </el-form-item>
        <el-form-item class="fenxiao-order-zy-page__filter-actions">
          <el-button type="primary" icon="el-icon-search" @click="select">查询</el-button>
          <el-button type="primary" icon="el-icon-refresh-left" @click="cleans">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="nf-table-card">
      <el-table v-loading="tableDataLoading" :data="tableData">
        <el-table-column prop="id" label="编号">
        </el-table-column>
        <el-table-column prop="userId" label="用户id">
          <template slot-scope="scope">
            <span class="fenxiao-order-zy-page__link" @click="updates(scope.row)">{{scope.row.userId}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="phone" label="手机号">
        </el-table-column>
        <el-table-column prop="commissionPrice" label="订单佣金">
        </el-table-column>
        <el-table-column prop="status" label="分销状态">
          <template slot-scope="scope">
            <span v-if="scope.row.status == 1">未到账</span>
            <span v-if="scope.row.status == 2">已到账</span>
          </template>
        </el-table-column>
        <el-table-column prop="detail" label="分销明细">
        </el-table-column>
        <el-table-column prop="createAt" label="创建时间">
        </el-table-column>
      </el-table>
      <div class="fenxiao-order-zy-page__pagination">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :page-sizes="[5, 10, 15, 20]"
          :page-size="size"
          :current-page="page"
          layout="total,sizes, prev, pager, next"
          :total="totalnum">
        </el-pagination>
      </div>
    </div>
  </div>
</template>

<script>
	export default {
	    data() {
	      return {
				size:5,
				page:1,
				userId:'',
				phone:'',
				totalnum:0,
				moneyFrom:0,
				formLabelWidth:'200px',
				activeName: 'first',
				tableDataLoading:false,
				tableData:[],
				status:0,
				statusnum: [
				  {
				    value: 0,
				    label: '全部'
				  }, {
				    value: 1,
				    label: '未到账'
				  }, {
				    value: 2,
				    label: '已到账'
				  }],
			}
		},
		filters: {
		  numFilter (value) {
		    let realVal = ''
		    if (!isNaN(value) && value!== '') {
		      // 截取当前数据到小数点后两位
		      realVal = parseFloat(value).toFixed(2)
		    } else {
		      realVal = '--'
		    }
		    return realVal
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
			// 详情跳转
			updates (row) {
			  this.$router.push({path: '/userDetail', query: {userId: row.userId}})
			},
			// 详情跳转
			updates (row) {
			  this.$router.push({path: '/userDetail', query: {userId: row.userId}})
			},
			selectTrigger(){
				this.size = 5
				this.page = 1
				this.dataSelect()
			},
			//搜索
			select(){
				this.page = 1
				this.size = 5
				this.dataSelect()
			},
			// 重置
			cleans(){
				this.status = 0
				this.phone = ''
				this.dataSelect()
			},
			// 获取订单数据
			dataSelect () {
			  let page = this.page-1
			  this.tableDataLoading = true
			  this.$http({
				url: this.$http.adornUrl('ordersRelation/list'),
				method: 'get',
				params: this.$http.adornParams({
					'page':page,
					'size':this.size,
					'userId':this.userId,
					'phone':this.phone,
					'status':this.status,
					'moneyFrom':this.moneyFrom
				})
			  }).then(({data}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableData = returnData.content
					this.totalnum = returnData.totalElements
			  })
			}
		},
		mounted() {
			this.dataSelect()
		}
	  };
</script>

<style scoped>
.fenxiao-order-zy-page__filter-select {
  width: 150px;
}

.fenxiao-order-zy-page__filter-input {
  width: 150px;
}

.fenxiao-order-zy-page__filter-actions .el-button + .el-button {
  margin-left: 12px;
}

.fenxiao-order-zy-page__link {
  color: #5cc477;
  cursor: pointer;
}

.fenxiao-order-zy-page__pagination {
  display: flex;
  justify-content: center;
  padding-top: 12px;
}
</style>
