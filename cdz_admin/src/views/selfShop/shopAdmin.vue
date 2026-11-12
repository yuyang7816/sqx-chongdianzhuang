<template>
	<div class="nf-page shop-admin-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">商品管理</span>
			<h2 class="nf-page-hero__title">商品管理</h2>
			<p class="nf-page-hero__desc">管理自营商品列表，支持按标题、类型、状态筛选查询，以及发布商品与天猫/京东/淘宝平台商品采集。</p>
			<div class="nf-page-hero__bar"></div>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">
				<i class="el-icon-search nf-filter-card__head-icon"></i>
				<span>筛选条件</span>
			</div>
			<el-form :inline="true" class="nf-filter-card__body" @submit.native.prevent>
				<el-form-item label="商品标题">
					<el-input
						class="shop-admin-page__filter-input"
						@keydown.enter.native="select"
						clearable
						placeholder="请输入商品标题"
						v-model="title">
					</el-input>
				</el-form-item>
				<el-form-item label="商品类型">
					<el-cascader
						class="shop-admin-page__filter-cascader"
						v-model="value"
						:options="classDatas"
						:show-all-levels="false"
						:props="{ expandTrigger: 'hover', label: 'name', value: 'id' }"
						@change="handleChange">
					</el-cascader>
				</el-form-item>
				<el-form-item label="商品状态">
					<el-select
						class="shop-admin-page__filter-select"
						v-model="status"
						placeholder="请选择商品状态"
						@change="select(status)">
						<el-option v-for="item in statusmain" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</el-form-item>
				<el-form-item class="shop-admin-page__filter-actions">
					<el-button type="primary" icon="el-icon-search" @click="select">查询</el-button>
					<el-button type="primary" icon="el-icon-refresh-left" @click="cleans">重置</el-button>
				</el-form-item>
			</el-form>
		</div>

		<div class="nf-table-card">
			<div class="shop-admin-page__toolbar">
				<el-button type="primary" icon="el-icon-plus" :disabled="!isAuth('shopAdmin:add')" @click="addShop">发布商品</el-button>
				<el-button type="primary" icon="el-icon-refresh" @click="refresh">刷新</el-button>
				<el-button type="primary" icon="el-icon-download" @click="collection(1)">天猫商品采集</el-button>
				<el-button type="primary" icon="el-icon-download" @click="collection(2)">京东商品采集</el-button>
				<el-button type="primary" icon="el-icon-download" @click="collection(3)">淘宝商品采集</el-button>
			</div>
		<el-table v-loading="tableDataLoading" :data="tableData.data">
			<el-table-column fixed prop="id" label="编号" width="80">
			</el-table-column>
			<el-table-column fixed prop="goods_image" label="商品图片">
				<template slot-scope="scope">
					<img :src="scope.row.goods_image" alt="" width="60" height="60" />
				</template>
			</el-table-column>
			<el-table-column prop="type" label="商品类型">
				<template slot-scope="scope">
					<span v-if="scope.row.type == null">无数据</span>
					<span v-else>{{ scope.row.type.name }}</span>
				</template>
			</el-table-column>
			<el-table-column prop="title" label="商品标题" width="200">
			</el-table-column>
			<el-table-column prop="originalPrice" label="商品原价">
				<template slot-scope="scope">
					<span style="color: #f56c6c">{{
            scope.row.goods_money.old_price | numFilter
          }}</span>
				</template>
			</el-table-column>
			<el-table-column prop="price" label="商品价格">
				<template slot-scope="scope">
					<span style="color: #f56c6c">{{
            scope.row.goods_money.new_price | numFilter
          }}</span>
				</template>
			</el-table-column>
			<el-table-column prop="memberPrice" label="会员价格">
				<template slot-scope="scope">
					<span style="color: #f56c6c">{{
            scope.row.goods_money.vip_price | numFilter
          }}</span>
				</template>
			</el-table-column>
			<el-table-column prop="commissionPrice" label="佣金">
				<template slot-scope="scope">
					<span style="color: #f56c6c">{{
            scope.row.goods_money.com_price | numFilter
          }}</span>
				</template>
			</el-table-column>

			<el-table-column prop="goods_sales" label="商品销量"> </el-table-column>
			<el-table-column prop="goods_money.buy_reason" label="必买理由" width="200">
			</el-table-column>
			<el-table-column label="精选好物" width="120">
				<template slot-scope="scope">
					<span v-if="scope.row.is_jingxuan == 0">否</span>
					<span v-if="scope.row.is_jingxuan == 1">是</span>
				</template>
			</el-table-column>
			<el-table-column label="首页商品" width="120">
				<template slot-scope="scope">
					<span v-if="scope.row.is_index == 0">否</span>
					<span v-if="scope.row.is_index == 1">是</span>
				</template>
			</el-table-column>
			<el-table-column label="每日推荐" width="120">
				<template slot-scope="scope">
					<span v-if="scope.row.is_tuijian == 0">否</span>
					<span v-if="scope.row.is_tuijian == 1">是</span>
				</template>
			</el-table-column>
			<el-table-column label="是否需要发货" width="150">
				<template slot-scope="scope">
					<span>{{ scope.row.is_fahuo }}</span>
				</template>
			</el-table-column>
			<el-table-column prop="createtime" label="创建时间" width="160">
			</el-table-column>
			<el-table-column fixed="right" label="上下架">
				<template slot-scope="scope">
					<el-switch v-model="scope.row.status" @change="change(scope.row.id, scope.row.status)"
						:active-value="openValue" :inactive-value="closeValue" active-color="#5cc477"
						inactive-color="#ff4949">
					</el-switch>
				</template>
			</el-table-column>
			<el-table-column prop="status" label="商品状态" fixed="right">
				<template slot-scope="scope">
					<span class="shop-admin-page__status" v-if="scope.row.status == 1">上架</span>
					<span class="shop-admin-page__status" v-if="scope.row.status == 0">下架</span>
				</template>
			</el-table-column>

			<el-table-column fixed="right" label="操作" width="180">
				<template slot-scope="scope">
					<el-button size="mini" type="primary" @click="discuss(scope.$index, scope.row)">商品评价
					</el-button>
					<el-button size="mini" type="primary" :disabled="!isAuth('shopAdmin:update')"
						@click="shopUpdates(scope.$index, scope.row)">修改
					</el-button>
					<!-- <el-button
	  		        size="mini"
	  		        type="danger"
                :disabled="!isAuth('shopAdmin:delete')"
	  		        @click="shopdeletes(scope.row)">删除
	  		      </el-button> -->
				</template>
			</el-table-column>
		</el-table>
			<div class="shop-admin-page__pagination">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30]" :page-size="size" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="tableData.total">
				</el-pagination>
			</div>
		</div>
		<!--天猫  选择商品弹框 -->
		<el-dialog title="商品列表" customClass="customWidth" :visible.sync="TMdialog" center width="85%">
			<div>
				<div style="margin:5px; display: inline-block">
					<span>商品关键字：</span>
					<el-input style="width: 150px" @keydown.enter.native="getTMCollection" clearable placeholder="请输入商品关键字"
						v-model="Search1"></el-input>&nbsp;&nbsp;&nbsp;&nbsp;
				</div>
				<div style="margin:5px; display: inline-block">
					<span>排序：</span>
					<el-select v-model="TMSearch2" placeholder="请选择">
						<el-option v-for="item in TMcollectionSort" :key="item.value" :label="item.name"
							:value="item.id">
						</el-option>
					</el-select>&nbsp;&nbsp;&nbsp;&nbsp;
				</div>
				<div style="margin: 2% 0; display: inline-block">
					<span>搜索模块 ：</span>
					<el-select v-model="TMSearch3" placeholder="请选择">
						<el-option v-for="item in TMsearchModule" :key="item.value" :label="item.name" :value="item.id">
						</el-option>
					</el-select>&nbsp;&nbsp;&nbsp;&nbsp;
				</div>
				<div style="margin:5px; display: inline-block">
					<span>搜索渠道：</span>
					<el-select v-model="TMSearch4" placeholder="请选择">
						<el-option v-for="item in TMsearchChannel" :key="item.value" :label="item.name"
							:value="item.id">
						</el-option>
					</el-select>&nbsp;&nbsp;&nbsp;&nbsp;
				</div>
				<div style="margin: 5px; display: inline-block">
					<span>页数：</span>
					<el-select v-model="search5" placeholder="请选择">
						<el-option v-for="item in searchPage" :key="item.value" :label="item.name" :value="item.id">
						</el-option>
					</el-select>&nbsp;&nbsp;&nbsp;&nbsp;
				</div>
				<div style="display: inline-block">
					<el-button style="margin-left: 15px" size="mini" type="primary" icon="document"
						@click="getTMCollection">查询</el-button>
					<el-button style="margin-left: 15px" size="mini" type="primary" icon="document" @click="cleans">重置
					</el-button>
				</div>
				<el-table v-loading="tableDataLoading1" :data="TMtableData">
					<el-table-column prop="coverImg" label="商品图片">
						<template slot-scope="scope">
							<img :src="scope.row.imageUrls[0]" alt="" width="60" height="60" />
						</template>
					</el-table-column>
					<el-table-column prop="title" label="商品标题" width="200">
					</el-table-column>
					<el-table-column prop="marketPrice" label="商品原价">
					</el-table-column>
					<el-table-column prop="price" label="商品价格"> </el-table-column>
					<el-table-column prop="price" label="会员价格"> </el-table-column>
					<el-table-column prop="seller" label="旗舰店"> </el-table-column>
					<el-table-column prop="sales" label="销量"> </el-table-column>
					<el-table-column label="操作" width="100">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" @click="shopXz(scope.row)">选择
							</el-button>
						</template>
					</el-table-column>
				</el-table>
			</div>
		</el-dialog>
		<!-- 京东 选择商品弹框 -->
		<el-dialog title="商品列表" customClass="customWidth" :visible.sync="JDdialog" center width="85%">
			<div>
				<div style="margin: 2% 0; display: inline-block">
					<span>商品标题：</span>
					<el-input style="width: 150px" @keydown.enter.native="getJDCollection" clearable placeholder="请输入商品标题"
						v-model="Search1"></el-input>&nbsp;&nbsp;&nbsp;&nbsp;
				</div>
				<div style="margin: 2% 0; display: inline-block">
					<span>排序：</span>
					<el-select v-model="JDSearch2" placeholder="请选择">
						<el-option v-for="item in JDcollectionSort" :key="item.value" :label="item.name"
							:value="item.id">
						</el-option>
					</el-select>&nbsp;&nbsp;&nbsp;&nbsp;
				</div>
				<div style="margin: 2% 0; display: inline-block">
					<span>搜索渠道：</span>
					<el-select v-model="JDSearch3" placeholder="请选择">
						<el-option v-for="item in JDsearchChannel" :key="item.value" :label="item.name"
							:value="item.id">
						</el-option>
					</el-select>&nbsp;&nbsp;&nbsp;&nbsp;
				</div>
				<div style="margin: 2% 0; display: inline-block">
					<span>页数：</span>
					<el-select v-model="search5" placeholder="请选择">
						<el-option v-for="item in searchPage" :key="item.value" :label="item.name" :value="item.id">
						</el-option>
					</el-select>&nbsp;&nbsp;&nbsp;&nbsp;
				</div>
				<div style="display: inline-block">
					<el-button style="margin-left: 15px" size="mini" type="primary" icon="document"
						@click="getJDCollection">查询</el-button>
				</div>
				<el-table v-loading="tableDataLoading1" :data="JDtableData">
					<el-table-column prop="coverImg" label="商品图片">
						<template slot-scope="scope">
							<img :src="scope.row.imageUrl" alt="" width="60" height="60" />
						</template>
					</el-table-column>
					<el-table-column prop="title" label="商品标题" width="200">
					</el-table-column>
					<el-table-column prop="price" label="商品原价"> </el-table-column>
					<el-table-column prop="price" label="商品价格"> </el-table-column>
					<el-table-column prop="price" label="会员价格"> </el-table-column>
					<el-table-column prop="shopName" label="店名"> </el-table-column>
					<el-table-column prop="attr" label="风格"> </el-table-column>
					<el-table-column label="操作" width="100">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" @click="shopXz(scope.row)">添加
							</el-button>
						</template>
					</el-table-column>
				</el-table>
			</div>
		</el-dialog>
		<!-- 淘宝 选择商品弹框 -->
		<el-dialog title="商品列表" customClass="customWidth" :visible.sync="TBdialog" center width="85%">
			<div>
				<div style="margin:5px; display: inline-block">
					<span>商品标题：</span>
					<el-input style="width: 150px" @keydown.enter.native="getTBCollection" clearable placeholder="请输入商品标题"
						v-model="Search1"></el-input>&nbsp;&nbsp;&nbsp;&nbsp;
				</div>
				<div style="margin:5px; display: inline-block">
					<span>排序：</span>
					<el-select v-model="TBSearch2" placeholder="请选择">
						<el-option v-for="item in TBcollectionSort" :key="item.value" :label="item.name"
							:value="item.id">
						</el-option>
					</el-select>&nbsp;&nbsp;&nbsp;&nbsp;
				</div>
				<div style="margin:5px;display: inline-block">
					<span>搜索渠道：</span>
					<el-select v-model="TBSearch3" placeholder="请选择">
						<el-option v-for="item in TBsearchChannel" :key="item.value" :label="item.name"
							:value="item.id">
						</el-option>
					</el-select>&nbsp;&nbsp;&nbsp;&nbsp;
				</div>
				<div style="margin:5px; display: inline-block">
					<span>页数：</span>
					<el-select v-model="search5" placeholder="请选择">
						<el-option v-for="item in searchPage" :key="item.value" :label="item.name" :value="item.id">
						</el-option>
					</el-select>&nbsp;&nbsp;&nbsp;&nbsp;
				</div>
				<div style="display: inline-block;margin:5px;">
					<el-button style="margin-left: 15px" size="mini" type="primary" icon="document"
						@click="getTBCollection">查询</el-button>
				</div>
				<el-table v-loading="tableDataLoading1" :data="TBtableData">
					<el-table-column prop="pic_url" label="商品图片">
						<template slot-scope="scope">
							<img :src="scope.row.pic_url" alt="" width="60" height="60" />
						</template>
					</el-table-column>
					<el-table-column prop="title" label="商品标题" width="200">
					</el-table-column>
					<el-table-column prop="marketPrice" label="商品原价"> </el-table-column>
					<el-table-column prop="price" label="商品价格"> </el-table-column>
					<el-table-column prop="price" label="会员价格"> </el-table-column>
					<!-- <el-table-column prop="vip_price" label="佣金"> </el-table-column> -->
					<el-table-column prop="sales" label="销量"> </el-table-column>
					<el-table-column label="操作" width="100">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" @click="shopXz(scope.row)">添加
							</el-button>
						</template>
					</el-table-column>
				</el-table>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				size: 10,
				// page:1,
				pagelist: [],
				openValue: 1,
				closeValue: 0,
				id: "",
				img: "",
				title: "",
				type: "",
				typeId: "",
				readonly: true,
				merchants: "",
				price: "",
				memberPrice: "",
				originalPrice: "",
				sales: "",
				status: "all",
				status2: "",
				descrition: "",
				totalnum: 0,
				imgUrl: "",
				linkUrl: "",
				value: [],
				classDatas: [],
				form1: {
					id: "",
					title: "",
					img: "",
					typeId: "",
					merchants: "",
					price: "",
					memberPrice: "",
					status: "",
					sales: "",
					descrition: "",
					originalPrice: "",
					createAt: "",
				},
				statusmain: [{
						value: "all",
						label: "全部",
					},
					{
						value: 1,
						label: "上架",
					},
					{
						value: 0,
						label: "下架",
					},
				],
				statusmain2: [{
						value: 1,
						label: "上架",
					},
					{
						value: 2,
						label: "下架",
					},
				],
				formLabelWidth: "200px",
				tableDataLoading: false,
				tableDataLoading1: false,
				dialogFormVisible2: false,
				dialogFormVisible3: false,
				typeDatas: [],
				advertData: [],
				tableData: [],
				TMdialog: false,
				JDdialog: false,
				TBdialog: false,
				TBtableData: [],
				TMtableData: [],
				JDtableData: [],
				TMcollectionSort: [{
						id: "s",
						name: "综合",
					},
					{
						id: "rq",
						name: "人气",
					},
					{
						id: "new",
						name: "新品",
					},
					{
						id: "d",
						name: "销量",
					},
					{
						id: "p",
						name: "价格由低到高",
					},
					{
						id: "pd",
						name: "价格由高到低",
					},
				],
				TMsearchModule: [{
						id: "default",
						name: "天猫商城",
					},
					{
						id: "tmallnk",
						name: "天猫国际",
					},
				],
				TMsearchChannel: [{
						id: "all",
						name: "全部",
					},
					{
						id: "newmall",
						name: "天猫",
					},
				],
				searchPage: [{
						id: 1,
						name: "第一页",
					},
					{
						id: 2,
						name: "第二页",
					},
					{
						id: 3,
						name: "第三页",
					},
					{
						id: 4,
						name: "第四页",
					},
					{
						id: 5,
						name: "第五页",
					},
					{
						id: 6,
						name: "第六页",
					},
					{
						id: 7,
						name: "第七页",
					},
					{
						id: 8,
						name: "第八页",
					},
					{
						id: 9,
						name: "第九页",
					},
					{
						id: 10,
						name: "第十页",
					},
				],
				Search1: 'TOS',
				TMSearch2: "s",
				TMSearch3: "default",
				TMSearch4: "all",
				search5: 1,
				JDcollectionSort: [{
						id: 1,
						name: '综合'
					},
					{
						id: 2,
						name: '最新上架'
					},
					{
						id: 3,
						name: '价格最低'
					},
					{
						id: 4,
						name: '价格最高'
					},
					{
						id: 5,
						name: '评论最多'
					},
					{
						id: 6,
						name: '销量'
					},
				],
				JDsearchChannel: [{
						id: 0,
						name: '综合'
					},
					{
						id: 1,
						name: '自营'
					}
				],
				JDSearch2: 1,
				JDSearch3: 0,
				TBcollectionSort: [{
						id: 'default',
						name: '综合'
					},
					{
						id: 'sale-desc',
						name: '销量'
					},
					{
						id: 'credit-desc',
						name: '信用'
					},
					{
						id: ' price-asc',
						name: '价格由低到高'
					},
					{
						id: 'price-desc',
						name: '价格由高到低'
					}
				],
				TBsearchChannel: [{
						id: 'all',
						name: '全部'
					},
					{
						id: 'newmall',
						name: '天猫'
					}
				],
				TBSearch2: 'default',
				TBSearch3: 'all',
				laiyuan:'',
				goodsData:{},
			};
		},
		filters: {
			numFilter(value) {
				let realVal = "";
				if (!isNaN(value) && value !== "") {
					// 截取当前数据到小数点后三位
					let tempVal = parseFloat(value).toFixed(3);
					realVal = tempVal.substring(0, tempVal.length - 1);
				} else {
					realVal = "--";
				}
				return realVal;
			},
		},
		computed: {
			page() {
				this.pagelist = this.$store.state.common.pagelist;
				var page = 1;
				for (let val of this.pagelist) {
					if (val.router == "shopAdmin") {
						page = val.page;
					}
				}
				return page;
			},
		},
		methods: {
			// 上下架
			change(id, val) {
				this.$http({
					url: this.$http.adornUrl2("/admin/goods/is_status"),
					method: "post",
					params: this.$http.adornParams({
						goods_id: id,
						type: val,
					}),
				}).then(({
					data
				}) => {
					this.$message({
						message: "操作成功",
						type: "success",
						duration: 1500,
					});
				});
			},
			// 刷新
			refresh() {
				this.shopSelect();
			},
			handleChange(value) {
				this.value.forEach((element) => {
					this.typeId = element;
				});
				this.shopSelect();
			},

			handleAvatarSuccess(file) {
				this.img = file.data;
			},
			handleAvatarSuccess2(file) {
				this.form1.img = file.data;
			},
			handleSizeChange(val) {
				this.size = val;
				this.shopSelect();
			},
			handleCurrentChange(val) {
				// this.page = val;
				for (let item of this.pagelist) {
					if (item.router == "shopAdmin") {
						item.page = val;
					}
				}
				this.$store.commit("common/updataPage", this.pagelist);
				this.shopSelect();
			},
			//搜索
			select() {
				// this.page = 1
				for (let item of this.pagelist) {
					if (item.router == "shopAdmin") {
						item.page = 1;
					}
				}
				this.$store.commit("common/updataPage", this.pagelist);
				this.size = 10;
				this.shopSelect();
			},
			// 重置
			cleans() {
				this.title = "";
				this.status = "all";
				this.typeId = "";
				this.value = [];
				this.shopSelect();
			},
			//发布商品
			addShop() {
				this.$router.push({
					path: "/shopPublish",
				});
			},
			// 修改商品
			shopUpdates(index, rows) {
				this.$router.push({
					path: "/shopAmend",
					query: {
						id: rows.id,
					},
				});
			},
			// 商品评价跳转
			discuss(index, rows) {
				this.$router.push({
					path: "/discuss",
					query: {
						goodsId: rows.id,
					},
				});
			},
			// 删除商品
			shopdeletes(row) {
				this.$confirm(`确定删除此条信息?`, "提示", {
						confirmButtonText: "确定",
						cancelButtonText: "取消",
						type: "warning",
					})
					.then(() => {
						this.$http({
							url: this.$http.adornUrl2("/goods/delete"),
							method: "get",
							params: this.$http.adornParams({
								id: row.id,
							}),
						}).then(({
							data
						}) => {
							this.$message({
								message: "删除成功",
								type: "success",
								duration: 1500,
								onClose: () => {
									this.shopSelect();
								},
							});
						});
					})
					.catch(() => {});
			},
			// 获取商品数据
			shopSelect() {
				this.tableDataLoading = true;
				this.$http({
					url: this.$http.adornUrl2("/admin/goods/goods_list"),
					method: "get",
					params: this.$http.adornParams({
						page: this.page,
						num: this.size,
						search_title: this.title,
						search_goods_cate: this.typeId,
						status: this.status,
					}),
				}).then(({
					data
				}) => {
					this.tableDataLoading = false;
					let returnData = data.data;
					this.tableData = returnData;
				});
			},
			// 商品分类
			dataSelect() {
				this.$http({
					url: this.$http.adornUrl2("/admin/goods/goods_cate"),
					method: "get",
					params: this.$http.adornParams({}),
				}).then(({
					data
				}) => {
					let returnData = data.data;
					this.classDatas = returnData;
				});
			},
			collection(e) {
				if (e == 1) {
					this.getTMCollection()
					this.TMdialog = true;
					this.laiyuan = '天猫'
				} else if (e == 2) {
					this.laiyuan = '京东'
					this.getJDCollection()
					this.JDdialog = true;
				} else {
					this.laiyuan = '淘宝'
					this.getTBCollection()
					this.TBdialog = true;
				}
			},
			//获取天猫商品数据
			getTMCollection() {
				this.tableDataLoading1 = true;
				this.$http({
					url: this.$http.adornUrl2("/admin/collectdata/tianmao_collect_data"),
					method: "post",
					data: this.$http.adornData({
						keyword: this.Search1,
						page: this.search5,
						order: this.TMSearch2,
						channel: this.TMSearch3,
						tab: this.TMSearch4,
					}),
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.TMtableData = data.data;
						this.tableDataLoading1 = false;
					}
				});
			},
			//获取京东商品数据
			getJDCollection() {
				this.tableDataLoading1 = true;
				this.$http({
					url: this.$http.adornUrl2("/admin/collectdata/jingdong_collect_data"),
					method: "post",
					data: this.$http.adornData({
						keyword: this.Search1,
						page: this.search5,
						order: this.JDSearch2,
						self: this.JDSearch3,
					}),
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.JDtableData = data.data;
						this.tableDataLoading1 = false;
					}
				});
			},
			//获取淘宝商品数据
			getTBCollection() {
				this.tableDataLoading1 = true;
				this.$http({
					url: this.$http.adornUrl2("/admin/collectdata/taobao_collect_data"),
					method: "post",
					data: this.$http.adornData({
						keyword: this.Search1,
						order: this.TBSearch2,
						tab: this.TBSearch3,
						page: this.search5,
					}),
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.TBtableData = data.data;
						this.tableDataLoading1 = false;
					}
				});
			},
			// 选择商品
			shopXz(row){
				// 天猫商品
				if(this.laiyuan=='天猫'){
					this.$http({
						url: this.$http.adornUrl2("/admin/collectdata/tianmao_collect_info"),
						method: "post",
						data: this.$http.adornData({
							itemId: row.id
						}),
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							console.log(data.data)
							var datas = {}
							datas.keyword = this.Search1
							datas.title = data.data.item.title
							datas.brand = data.data.item.groupProps[0].基本信息[0].品牌
							datas.goods_image = data.data.item.images[0]
							datas.goods_images = data.data.item.images
							datas.goods_salse = data.data.item.vagueSellCount.slice(0,-1)
							datas.old_price = data.data.item.marketPriceRange
							datas.new_price = data.data.item.priceRange
							datas.goods_info = data.data.item.desc
							datas.chandi = data.data.item.location
							datas.canshu = data.data.item.groupProps[0].基本信息
							datas.attr = data.data.item.props
							datas.sku = data.data.item.sku
							datas.buy_reason = data.data.item.subTitle
							datas.sku.splice(0, 1)
							this.confirm(datas)
							
						}else{
							this.$message({
								message: data.msg,
								type: "warning",
								duration: 1500,
								onClose: () => {
								},
							});
						}
					});
				}else if(this.laiyuan=='京东'){
					this.$http({
						url: this.$http.adornUrl2("/admin/collectdata/jingdong_collect_info"),
						method: "post",
						data: this.$http.adornData({
							itemId: row.itemId
						}),
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							console.log(data.data)
							var datas = {}
							datas.keyword = this.Search1
							datas.name = data.data.item.name
							datas.brand = data.data.item.brandName
							datas.goods_image = data.data.item.images[0]
							datas.goods_images = data.data.item.images
							datas.old_price = data.data.item.originalPrice
							datas.new_price = data.data.item.price
							datas.goods_info = data.data.item.desc
							if(data.data.seller.location){
								datas.chandi = data.data.seller.location
							}else{
								datas.chandi = ''
							}
							
							datas.canshu = data.data.item.pcAttributes
							datas.attrname = data.data.item.saleProp
							datas.attrvalue = data.data.item.skuProps
							datas.sku = data.data.item.sku
							// datas.sku.splice(0, 1)
							this.confirmJd(datas)
							console.log('京东商品详情···',datas)
							
						}else{
							this.$message({
								message: data.msg,
								type: "warning",
								duration: 1500,
								onClose: () => {
								},
							});
						}
					});
					
				}else if(this.laiyuan=='淘宝'){
					this.$http({
						url: this.$http.adornUrl2("/admin/collectdata/taobao_collect_info"),
						method: "post",
						data: this.$http.adornData({
							itemId: row.id
						}),
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							console.log(data.data)
							var datas = {}
							datas.keyword = this.Search1
							datas.title = data.data.item.title
							datas.brand = data.data.item.groupProps[0].基本信息[0].品牌
							datas.goods_image = data.data.item.images[0]
							datas.goods_images = data.data.item.images
							datas.goods_salse = data.data.item.vagueSellCount.slice(0,-1)
							datas.old_price = data.data.item.marketPriceRange
							datas.new_price = data.data.item.priceRange
							datas.goods_info = data.data.item.desc
							datas.chandi = data.data.item.location
							datas.canshu = data.data.item.groupProps[0].基本信息
							datas.attr = data.data.item.props
							datas.sku = data.data.item.sku
							datas.buy_reason = data.data.item.subTitle
							datas.sku.splice(0, 1)
							this.confirm(datas)
							
						}else{
							this.$message({
								message: data.msg,
								type: "warning",
								duration: 1500,
								onClose: () => {
								},
							});
						}
					});
				}
				
			},
			// 淘宝、天猫确认选择
			confirm(datas){
				this.$http({
					url: this.$http.adornUrl2("/admin/collectdata/add_collect_goods"),
					method: "post",
					data: this.$http.adornData(datas),
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						console.log('选择结果：',data.data)
						this.$message({
							message: data.msg,
							type: "success",
							duration: 1500,
							onClose: () => {
							},
						});
						this.shopSelect();
					}else{
						this.$message({
							message: data.msg,
							type: "warning",
							duration: 1500,
							onClose: () => {
							},
						});
					}
				});
			},
			// 京东确认选择
			confirmJd(datas){
				this.$http({
					url: this.$http.adornUrl2("/admin/collectdata/add_collect_jdgoods"),
					method: "post",
					data: this.$http.adornData(datas),
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						console.log('选择结果：',data.data)
						this.$message({
							message: data.msg,
							type: "success",
							duration: 1500,
							onClose: () => {
							},
						});
						this.shopSelect();
					}else{
						this.$message({
							message: data.msg,
							type: "warning",
							duration: 1500,
							onClose: () => {
							},
						});
					}
				});
			}
		},
		mounted() {
			// this.title = this.$route.query.title
			console.log("标题：", this.title);
			this.shopSelect();
			this.dataSelect();
		},
		watch: {
			$route: "title",
			$route: "shopSelect",
		},
	};
</script>

<style scoped>
.shop-admin-page__filter-input {
	width: 200px;
}

.shop-admin-page__filter-cascader {
	width: 150px;
}

.shop-admin-page__filter-select {
	width: 150px;
}

.shop-admin-page__filter-actions .el-button + .el-button {
	margin-left: 12px;
}

.shop-admin-page__toolbar {
	display: flex;
	flex-wrap: wrap;
	align-items: center;
	gap: 12px;
	margin-bottom: 12px;
}

.shop-admin-page__status {
	color: #5cc477;
	cursor: pointer;
}

.shop-admin-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}
</style>
