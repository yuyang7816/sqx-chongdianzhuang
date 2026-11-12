x<template>
	<div class="nf-page ongoing-activities-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">活动管理</span>
			<h2 class="nf-page-hero__title">进行中活动</h2>
			<p class="nf-page-hero__desc">查看当前进行中的活动列表，支持按活动名称、城市、联系电话及发布人昵称筛选，可查看报名/签到列表或结束活动。</p>
			<div class="nf-page-hero__bar"></div>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">
				<i class="el-icon-search nf-filter-card__head-icon"></i>
				<span>筛选条件</span>
			</div>
			<el-form :inline="true" class="nf-filter-card__body" @submit.native.prevent>
				<el-form-item label="活动名称">
					<el-input class="ongoing-activities-page__filter-input" @keydown.enter.native="phoneSelect"
						placeholder="请输入活动名称" v-model="activityNameT">
					</el-input>
				</el-form-item>
				<el-form-item label="城市">
					<el-input class="ongoing-activities-page__filter-input" @keydown.enter.native="phoneSelect"
						placeholder="请输入城市" v-model="cityT">
					</el-input>
				</el-form-item>
				<el-form-item label="联系电话">
					<el-input class="ongoing-activities-page__filter-input" @keydown.enter.native="phoneSelect"
						placeholder="请输入联系电话" v-model="phoneT">
					</el-input>
				</el-form-item>
				<el-form-item label="发布人昵称">
					<el-input class="ongoing-activities-page__filter-input" @keydown.enter.native="phoneSelect"
						placeholder="请输入发布人昵称" v-model="userNameT">
					</el-input>
				</el-form-item>
				<el-form-item class="ongoing-activities-page__filter-actions">
					<el-button type="primary" icon="el-icon-search" @click="phoneSelect">查询</el-button>
					<el-button type="primary" icon="el-icon-refresh-left" @click="cleans2">重置</el-button>
				</el-form-item>
			</el-form>
		</div>

		<div class="nf-table-card">
		<el-table v-loading="tableDataLoading" :data="choicenData.list">
			<el-table-column prop="activityId" label="编号" width="80" fixed="left">
			</el-table-column>
			<el-table-column prop="nickName" label="发布人昵称" fixed="left">
				<template slot-scope="scope">
					　<span class="ongoing-activities-page__link"
						@click="updatesvideo(scope.row.userId)">{{scope.row.userName?scope.row.userName:'未绑定'}}</span>
				</template>
			</el-table-column>
			<el-table-column prop="activityPhone" label="联系电话" width="100">
			</el-table-column>
			<!-- <el-table-column prop="gameName" label="活动类型">
			</el-table-column> -->
			
			<el-table-column prop="shopName" label="店铺名称" width="150">
			</el-table-column>
			<el-table-column prop="scShop.shopAddress" label="商家地址" width="150">
			</el-table-column>
			<el-table-column prop="activityName" label="活动名称" width="150">
			</el-table-column>
			<el-table-column prop="activityImg" label="主图" width="200">
				<template slot-scope="scope">
					<div v-if="scope.row.activityImg">
						<div v-for="(item,index) in scope.row.activityImg.split(',')" :key="index"
							style="display: inline-block; margin: 3px;">
							<el-popover placement="top-start" title="" trigger="hover">
								<img style="width: 50px; height: 50px" :src="item" alt="" slot="reference">
								<img style="width: 300px; height: auto" :src="item" alt="">
							</el-popover>
						</div>
					</div>
			
				</template>
			</el-table-column>
			<el-table-column prop="activityDetailsImg" label="展示图" width="200">
				<template slot-scope="scope">
					<div v-if="scope.row.activityDetailsImg">
						<div v-for="(item,index) in scope.row.activityDetailsImg.split(',')" :key="index"
							style="display: inline-block; margin: 3px;">
							<el-popover placement="top-start" title="" trigger="hover">
								<img style="width: 50px; height: 50px" :src="item" alt="" slot="reference">
								<img style="width: 300px; height: auto" :src="item" alt="">
							</el-popover>
						</div>
					</div>
				</template>
			</el-table-column>
			<!-- <el-table-column prop="activityContent" label="活动规则" width="250">
				<template slot-scope="scope">
					<el-popover placement="top-start" title="" trigger="hover">
						<div style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;"
							slot="reference">{{scope.row.activityContent}}</div>
						<div style="width: 300px;">{{scope.row.activityContent}}</div>
					</el-popover>
				</template>
			</el-table-column> -->
			<el-table-column prop="activityDetails" label="活动须知" width="250">
				<template slot-scope="scope">
					<el-popover placement="top-start" title="" trigger="hover">
						<div style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;"
							slot="reference">{{scope.row.activityDetails}}</div>
						<div style="width: 300px;">{{scope.row.activityDetails}}</div>
					</el-popover>
				</template>
			</el-table-column>
			<el-table-column prop="activityDate" label="活动开始时间" width="100">
			</el-table-column>
			<el-table-column prop="activityEndTime" label="活动结束时间" width="100">
				<template slot-scope="scope">
					<span>{{scope.row.activityEndTime?scope.row.activityEndTime:'未设置'}}</span>
				</template>
			</el-table-column>
			<el-table-column prop="counts" label="已报名人数">
			</el-table-column>
			<el-table-column prop="signCounts" label="已签到人数">
			</el-table-column>
			<el-table-column prop="isSex" label="是否限制男女">
				<template slot-scope="scope">
					<div v-if="scope.row.isSex==1">是</div>
					<div v-else>否</div>
				</template>
			</el-table-column>
			<el-table-column prop="giftMouseNum" label="活动人数" width="160">
				<template slot-scope="scope">
			
					<div v-if="scope.row.isSex==1">
						<div>男生人数：{{scope.row.activityMan}}</div>
						<div>女生人数：{{scope.row.activityGirl}}</div>
					</div>
					<div v-else>
						<div>总人数：{{scope.row.activityGirl}}</div>
					</div>
				</template>
			</el-table-column>
			
			<el-table-column prop="activityEndDate" label="报名截止时间" width="160">
			</el-table-column>
			<el-table-column prop="paymentType" label="费用方式">
				<template slot-scope="scope">
					<div v-if="scope.row.paymentType==1">线上收费</div>
					<div v-if="scope.row.paymentType==2">免费</div>
					<div v-if="scope.row.paymentType==3">线下收费</div>
				</template>
			</el-table-column>
			<el-table-column prop="price" label="报名费">
			</el-table-column>
			<el-table-column prop="auditContent" label="审核理由" width="160">
			</el-table-column>
			<el-table-column prop="status" label="状态" width="100" fixed='right'>
				<template slot-scope="scope">
					<span style="color: #5cc477;" v-if="scope.row.status === 1 ">报名中</span>
					<span style="color: #13ce66;" v-if="scope.row.status === 2 ">进行中</span>
					<span style="color: #ff4949;" v-if="scope.row.status === 3 ">已结束</span>
					<span style="color: #999;" v-if="scope.row.status === 5 ">已取消</span>
					<span style="color: orange;" v-if="scope.row.status === 6 ">待开始</span>
					<span style="color: #ff4949;" v-if="scope.row.status === 7 ">待审核</span>
					<span style="color: #999;" v-if="scope.row.status === 8 ">已拒绝</span>
			
				</template>
			</el-table-column>
			<el-table-column prop="createTime" label="创建时间" width="160">
			</el-table-column>
			<el-table-column label="操作" prop="id" width="200" fixed='right'>
				<template slot-scope="scope">
					<el-button size="mini" type="primary" style="margin: 3px;" plain
						@click="shenhe(scope.row)">报名列表</el-button>
					<el-button size="mini" type="warning" style="margin: 3px;" plain
						@click="qiandao(scope.row)">签到列表</el-button>
					<!-- <el-button size="mini" type="warning" style="margin: 3px;" plain
						@click="dituBtn(scope.row)">活动地图</el-button> -->
					<el-button size="mini" type="primary" :disabled="!isAuth('locality:update')" style="margin: 3px;"
						@click="jieshu(scope.row)">结束活动
					</el-button>
					<!-- <el-button size="mini" type="primary" style="margin: 3px;" plain
						@click="buhuo(scope.row)">捕获记录</el-button>
					<el-button size="mini" type="warning" style="margin: 3px;"
						@click="xiangce( scope.row)">活动相册</el-button>
					<el-button size="mini" type="primary" @click="complainDetails(scope.row)" style="margin: 3px;">聊天内容
					</el-button> -->
				</template>
			</el-table-column>
		</el-table>
		<div class="ongoing-activities-page__pagination">
			<el-pagination @size-change="handleSizeChange1" @current-change="handleCurrentChange1"
				:page-sizes="[5,10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
				layout="total,sizes, prev, pager, next,jumper" :total="choicenData.totalCount">
			</el-pagination>
		</div>
		</div>
		<!-- 报名列表 -->
		<el-dialog title="报名列表" :visible.sync="dialogFormVisible8" center width="80%">
			<el-table width="780px" v-loading="tableDataLoading4" :data="baomingData.list">
				<el-table-column label="编号" prop="activityApplyId" width="80">
				</el-table-column>
				<el-table-column prop="nickName" label="昵称">
					<template slot-scope="scope">
						<span
							v-if="scope.row.userEntity">{{scope.row.userEntity? scope.row.userEntity.userName : '未绑定'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="phone" label="手机号">
					<template slot-scope="scope">
						<span
							v-if="scope.row.userEntity">{{scope.row.userEntity? scope.row.userEntity.phone : '未绑定'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="phone" label="性别">
					<template slot-scope="scope">
						<span>{{scope.row.sex==1 ? '男' : '女'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="role" label="角色">
					<template slot-scope="scope">
						<span v-if="scope.row.role==1">猫</span>
						<span v-else-if="scope.row.role==2">老鼠</span>
						<span v-else>未分配角色</span>
					</template>
				</el-table-column>
				<el-table-column prop="ordersNo" label="订单号">
				</el-table-column>
				<el-table-column prop="payMoney" label="支付金额">
					<template slot-scope="scope">
						　<span style="color: red;cursor: pointer;"
							@click="updatesvideo(scope.row.payMoney)">{{scope.row.payMoney?scope.row.payMoney:'0'}}</span>
					</template>
				</el-table-column>

				<el-table-column prop="nickName" label="一级佣金">
					<template slot-scope="scope">
						　<span style="color: red;cursor: pointer;"
							@click="updatesvideo(scope.row.zhiUserId)">{{scope.row.zhiMoney?scope.row.zhiMoney:'0'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="zhiUserName" label="一级用户昵称">
					<template slot-scope="scope">
						　<span style="color: #13ce66;cursor: pointer;"
							@click="updatesvideo(scope.row.zhiUserId)">{{scope.row.zhiUserName?scope.row.zhiUserName:'-'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="nickName" label="二级佣金">
					<template slot-scope="scope">
						　<span style="color: red;cursor: pointer;"
							@click="updatesvideo(scope.row.feiUserId)">{{scope.row.feiMoney?scope.row.feiMoney:'0'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="feiUserName" label="二级用户昵称">
					<template slot-scope="scope">
						　<span style="color: #13ce66;cursor: pointer;"
							@click="updatesvideo(scope.row.zhiUserId)">{{scope.row.feiUserName?scope.row.feiUserName:'-'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="pingMoney" label="平台佣金">
					<template slot-scope="scope">
						　<span
							style="color: red;cursor: pointer;">{{scope.row.pingMoney?scope.row.pingMoney:'0'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间">
				</el-table-column>
				<el-table-column label="状态" width="120">
					<template slot-scope="scope">
						<span v-if="scope.row.status==1" style="color: #f56c6c;">已报名</span>
						<span v-if="scope.row.status==2" style="color: #5cc477;">进行中</span>
						<span v-if="scope.row.status==3" style="color: #f56c6c;">已结束</span>
						<span v-if="scope.row.status==4" style="color: chocolate;">已签到</span>
						<span v-if="scope.row.status==5" style="color: #999;">已取消</span>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChangeBm" @current-change="handleCurrentChangeBm"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="limitB" :current-page="pageB"
					layout="total,sizes, prev, pager, next,jumper" :total="baomingData.totalCount">
				</el-pagination>
			</div>
		</el-dialog>
		<!-- 签到列表 -->
		<el-dialog title="签到列表" :visible.sync="dialogFormVisibleQd" center width="80%">
			<el-table v-loading="tableDataLoading4" :data="baomingData.records">
				<el-table-column prop="id" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="userName" label="签到用户">
					<template slot-scope="scope">
						　<span v-if="scope.row.userName" style="color: #5cc477;cursor: pointer;"
							@click="updatesvideo(scope.row.userId)">
							{{scope.row.userName? scope.row.userName : '未绑定'}}
						</span>
					</template>
				</el-table-column>
				<el-table-column prop="phone" label="手机号">
				</el-table-column>
				<el-table-column prop="title" label="活动名称">
				</el-table-column>
				<el-table-column prop="createAt" label="签到时间">
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChangeQd" @current-change="handleCurrentChangeQd"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="limitB" :current-page="pageB"
					layout="total,sizes, prev, pager, next,jumper" :total="baomingData.total">
				</el-pagination>
			</div>
		</el-dialog>
		<!-- 捕获记录  -->
		<el-dialog title="捕获记录" :visible.sync="dialogFormVisibleBh" center width="80%">
			<div style="position: relative;display: inline-block;margin:5px;">
				<span>手机号:</span>
				<el-input style="width: 200px;" @keydown.enter.native="buhuoBtns" placeholder="请输入手机号" v-model="phoneB">
				</el-input>&nbsp;&nbsp;
			</div>
			<el-button size="mini" type="primary" style="margin: 3px;" @click="buhuoBtns()">查询
			</el-button>
			<el-table v-loading="tableDataLoadingB" :data="baomingData.list">
				<el-table-column prop="id" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="userName" label="用户昵称" width="120">
					<template slot-scope="scope">
						　<span v-if="scope.row.userName" style="color: #5cc477;cursor: pointer;"
							@click="updatesvideo(scope.row.userId)">
							{{scope.row.userName? scope.row.userName : '未绑定'}}
						</span>
					</template>
				</el-table-column>
				<el-table-column prop="phone" label="手机号" width="160">
				</el-table-column>
				<el-table-column prop="content" label="内容">
				</el-table-column>
				<el-table-column prop="createAt" label="创建时间" width="160">
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChangeBh" @current-change="handleCurrentChangeBh"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="limitB" :current-page="pageB"
					layout="total,sizes, prev, pager, next,jumper" :total="baomingData.totalCount">
				</el-pagination>
			</div>
		</el-dialog>
		<!-- 活动相册  -->
		<el-dialog title="活动相册" :visible.sync="dialogFormVisibleXc" center width="80%">
			<el-table v-loading="tableDataLoadingB" :data="baomingData.list">
				<el-table-column prop="activityPhotoId" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="userName" label="上传用户" width="120">
					<template slot-scope="scope">
						　<span v-if="scope.row.userName" style="color: #5cc477;cursor: pointer;"
							@click="updatesvideo(scope.row.userId)">
							{{scope.row.userName? scope.row.userName : '未绑定'}}
						</span>
					</template>
				</el-table-column>
				<el-table-column prop="phone" label="手机号" width="120">
				</el-table-column>
				<el-table-column prop="photo" label="上传图片">
					<template slot-scope="scope">
						<div v-if="scope.row.photo">
							<div v-for="(item,index) in scope.row.photo.split(',')" :key="index"
								style="display: inline-block; margin: 3px;">
								<el-popover placement="top-start" title="" trigger="hover">
									<img style="width: 50px; height: 50px" :src="item" alt="" slot="reference">
									<img style="width: 300px; height: auto" :src="item" alt="">
								</el-popover>
							</div>
						</div>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="上传时间" width="160">
				</el-table-column>
				<el-table-column prop="photo" label="操作" width="150">
					<template slot-scope="scope">
						<el-button size="mini" type="danger" :disabled="!isAuth('locality:delete')" style="margin: 3px;"
							@click="choideleteDe(scope.row)">删除
						</el-button>
					</template>
				</el-table-column>

			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChangeXc" @current-change="handleCurrentChangeXc"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="limitB" :current-page="pageB"
					layout="total,sizes, prev, pager, next,jumper" :total="baomingData.totalCount">
				</el-pagination>
			</div>
		</el-dialog>
		<!-- 聊天内容 -->
		<el-dialog title="聊天内容" :visible.sync="dialogFormVisible" center width="80%">
			<div style="margin:2% 0;display: inline-block;">
				<span>内容:</span>
				<el-input style="width: 150px;" @keydown.enter.native="select1" clearable placeholder="请输入聊天内容"
					v-model="content">
				</el-input>
			</div>
			<div style="display: inline-block;">
				<el-button style='margin-left:15px;' size="mini" type="primary" icon="document"
					@click="select1">查询</el-button>
				<el-button style='margin-left:15px;' size="mini" type="primary" icon="document"
					@click="cleans1">重置</el-button>
			</div>
			<el-table v-loading="tableDataLoading" :data="userData.list">
				<el-table-column prop="activityChatContentId" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="userName" label="用户昵称">
					<template slot-scope="scope">
						<div style="color: #5cc477;cursor: pointer;" @click="updates(scope.row.userId)">
							{{scope.row.userName ? scope.row.userName : '未绑定'}}</div>
					</template>
				</el-table-column>
				<el-table-column prop="title" label="用户头像">
					<template slot-scope="scope">
						<div v-if="scope.row.avatar == null || scope.row.avatar == ''">
							暂无图片
						</div>
						<div v-else>
							<el-popover placement="top-start" title="" trigger="hover">
								<img style="width: 50px; height: 50px" :src="scope.row.avatar" alt="" slot="reference">
								<img style="width: 200px; height: 200px" :src="scope.row.avatar" alt="">
							</el-popover>
						</div>
					</template>

				</el-table-column>
				<el-table-column prop="content" label="信息内容" width="280">
					<template slot-scope="scope">
						<div v-if="scope.row.messageType==1">{{scope.row.content}}</div>
						<div v-if="scope.row.messageType==2">
							<el-popover placement="top-start" title="" trigger="hover">
								<img style="width: 50px; height: 50px" :src="scope.row.content" alt="" slot="reference">
								<img style="width: 200px; height: 200px" :src="scope.row.content" alt="">
							</el-popover>
						</div>
						<div v-if="scope.row.messageType==3">
							<audio :src="scope.row.content" type="audio/wav" controls="controls"></audio>
						</div>
						<div v-if="scope.row.messageType==4">
							<el-popover placement="top-start" title="" trigger="hover">
								<img style="width: 50px; height: 50px"
									:src="$http.adornUrl('emoji/'+scope.row.content)" alt=""
									slot="reference">
								<img style="width: 200px; height: 200px"
									:src="$http.adornUrl('emoji/'+scope.row.content)" alt="">
							</el-popover>
						</div>

						<!-- <div  v-if="scope.row.messageType==4">
		          <el-popover placement="top-start" title="" trigger="hover">
		            <img style="width: 50px; height: 50px" :src="$http.adornUrl('emoji/'+scope.row.content)" alt="" slot="reference">
		            <img style="width: 200px; height: 200px" :src="$http.adornUrl('emoji/'+scope.row.content)" alt="">
		          </el-popover>
		        </div> -->

					</template>
				</el-table-column>
				<el-table-column prop="contentCount" label="用户是否删除" width="100">
				  <template slot-scope="scope">
				    <span style="color: #ff4949;" v-if="scope.row.isDelete==1 ">用户已删除</span>
				    <span v-else>否</span>
				  </template>
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="150">
				</el-table-column>
				<el-table-column prop="createTime" label="操作" width="100" fixed="right">
					<template slot-scope="scope">
						<el-button size="mini" type="danger" :disabled="!isAuth('locality:delete')"
							@click="deleteuser(scope.row)">删除</el-button>
					</template>
				</el-table-column>

			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChangeLtnr" @current-change="handleCurrentChangeLtnr"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="limitB" :current-page="pageB"
					layout="total,sizes, prev, pager, next" :total="userData.totalCount">
				</el-pagination>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	export default {

		data() {
			return {
				size: 5,
				page: 1,
				limit: 10,
				tableDataLoading: true,
				tableDataLoading4: false,
				dialogFormVisible8: false,
				homeData: [],
				homeData1: [{
					id: 0,
					name: '全部'
				}],
				choicenData: {},
				id: '',
				userNameT: '',
				activityNameT: '',
				cityT: '',
				phoneT: '',
				classifyIds: '',
				statusT: 2,
				activityId: '',
				pageB: 1,
				limitB: 10,
				baomingData: {},
				dialogFormVisibleQd: false,
				dialogFormVisibleBh: false,
				dialogFormVisibleXc: false,
				dialogFormVisible: false,
				tableDataLoadingB: false,
				userData: {},
				content: '',
				isFabuGame: 1,
				isBaomingfei: 1,
				phoneB: '',
			}
		},
		methods: {
			handleSizeChange1(val) {
				this.size = val
				this.InformationSelect()
			},
			handleCurrentChange1(val) {
				this.page = val
				this.InformationSelect()
			},

			handleSizeChangeBm(val) {
				this.limitB = val
				this.baomingSelect()
			},
			handleCurrentChangeBm(val) {
				this.pageB = val
				this.baomingSelect()
			},
			handleSizeChangeQd(val) {
				this.limitB = val
				this.qiandaoSelect()
			},
			handleCurrentChangeQd(val) {
				this.pageB = val
				this.qiandaoSelect()
			},
			handleSizeChangeBh(val) {
				this.limitB = val
				this.buhuoSelect()
			},
			handleCurrentChangeBh(val) {
				this.pageB = val
				this.buhuoSelect()
			},
			handleSizeChangeXc(val) {
				this.limitB = val
				this.xiangceSelect()
			},
			handleCurrentChangeXc(val) {
				this.pageB = val
				this.xiangceSelect()
			},
			handleSizeChangeLtnr(val) {
				this.limitB = val;
				this.neirongSelect(this.activityId)
			},
			handleCurrentChangeLtnr(val) {
				this.pageB = val;
				this.neirongSelect(this.activityId)
			},
			// 查询
			phoneSelect() {
				this.page = 1
				this.InformationSelect()
			},
			// 重置
			cleans2() {
				this.activityNameT = ''
				this.cityT = ''
				this.phoneT = ''
				this.userNameT = ''
				this.classifyIds = ''
				this.page = 1
				this.InformationSelect()
			},
			// 获取活动类型启用列表
			homeSelect() {
				this.$http({
					url: this.$http.adornUrl(`game/queryGame`),
					method: 'get',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					let returnData = data.data
					this.homeData1 = [{
						id: '',
						gameName: '全部'
					}]
					if (data.data) {
						for (var i in data.data) {
							this.homeData1.push(data.data[i])
							data.data[i].state = Number(data.data[i].state)
						}
					}
					this.homeData = data.data
				})
			},
			// 信息数据
			InformationSelect() {
				this.tableDataLoading = true
				this.userId = this.$store.state.user.id
				this.$http({
					url: this.$http.adornUrl('activity/selectActivityList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.size,
						'userName': this.userNameT,
						'phone': this.phoneT,
						'status': this.statusT,
						'activityName': this.activityNameT,
						'city': this.cityT,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.choicenData = returnData


				})
			},
			// 报名列表弹框
			shenhe(row) {
				this.activityId = row.activityId
				this.pageB = 1
				this.dialogFormVisible8 = true
				this.baomingSelect()
			},
			// 获取报名列表
			baomingSelect() {
				this.tableDataLoading4 = true
				this.$http({
					url: this.$http.adornUrl('activityApply/selectActivityApplyList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.pageB,
						'limit': this.limitB,
						'activityId': this.activityId,
						'activityName': '',
						'userName': '',
						'phone': '',
						'status': '',
					})
				}).then(({
					data
				}) => {
					console.log('', data)
					if (data.code == 0) {
						this.tableDataLoading4 = false
						if (data.data.list.length > 0) {
							for (var i in data.data.list) {
								let payMoney = ''
								let zhiMoney = ''
								let feiMoney = ''
								if (data.data.list[i].payMoney) {
									payMoney = Number(data.data.list[i].payMoney)
								} else {
									payMoney = 0
								}
								if (data.data.list[i].zhiMoney) {
									zhiMoney = Number(data.data.list[i].zhiMoney)
								} else {
									zhiMoney = 0
								}
								if (data.data.list[i].feiMoney) {
									feiMoney = Number(data.data.list[i].feiMoney)
								} else {
									feiMoney = 0
								}
								data.data.list[i].pingMoney = (payMoney - zhiMoney - feiMoney).toFixed(2)
							}
						}
						let returnData = data.data
						this.baomingData = returnData
					} else {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: data.msg,
							type: 'warning'
						});
					}
				})
			},
			// 详情跳转
			updatesvideo(userId) {
				if (userId) {
					this.$router.push({
						path: '/userDetail',
						query: {
							userId: userId
						}
					})
				}

			},
			// 签到列表弹框
			qiandao(row) {
				this.activityId = row.activityId
				this.pageB = 1
				this.dialogFormVisibleQd = true
				this.qiandaoSelect()
			},
			// 获取签到列表
			qiandaoSelect() {
				this.tableDataLoading4 = true
				this.$http({
					url: this.$http.adornUrl(`message/selectActivitySignList`),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.pageB,
						'limit': this.limitB,
						'state': 110,
						'platform': '',
						'activityId': this.activityId
					})
				}).then(({
					data
				}) => {
					console.log('', data)
					if (data.code == 0) {
						this.tableDataLoading4 = false
						let returnData = data.data
						this.baomingData = returnData
					} else {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: data.msg,
							type: 'warning'
						});
					}
				})
			},
			// 活动地图
			dituBtn(row) {
				this.$router.push({
					path: '/shifuScheduling',
					query: {
						activityId: row.activityId,
						longitude: row.activityLongitude,
						latitude: row.activityLatitude,
						areaId: row.areaId
					}
				})
			},
			// 结束活动
			jieshu(row) {
				this.$confirm(`确定结束该活动?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('activity/endActivity'),
						method: 'post',
						params: this.$http.adornParams({
							'activityId': row.activityId,
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
									this.InformationSelect()
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
			// 捕获记录
			buhuo(row) {
				this.activityId = row.activityId
				this.pageB = 1
				this.phoneB = ''
				this.dialogFormVisibleBh = true
				this.buhuoSelect()
			},
			buhuoSelect() {
				this.tableDataLoadingB = true
				this.$http({
					url: this.$http.adornUrl(`message/page/100/${this.pageB}/${this.limitB}`),
					method: 'get',
					params: this.$http.adornParams({
						// 'page': this.pageB,
						// 'limit': this.limitB,
						// 'userName': '',
						// 'activityName': '',
						'phone': this.phoneB,
						'platformId': this.activityId
					})
				}).then(({
					data
				}) => {
					console.log('', data)
					this.tableDataLoadingB = false
					if (data.code == 0) {
						let returnData = data.data
						this.baomingData = returnData
					} else {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: data.msg,
							type: 'warning'
						});
					}
				})
			},
			// 围栏地图
			selectWl(row) {
				if (row) {
					this.$router.push({
						path: '/riderSchedulingFw',
						query: {
							contexts: JSON.stringify(row),
							from: 'loc'
						}
					})
				}
			},
			// 活动相册弹框
			xiangce(row) {
				this.activityId = row.activityId
				this.pageB = 1
				this.dialogFormVisibleXc = true
				this.xiangceSelect()
			},
			xiangceSelect() {
				this.tableDataLoadingB = true
				this.$http({
					url: this.$http.adornUrl('activityPhoto/selectActivityPhotoList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.pageB,
						'limit': this.limitB,
						'userName': '',
						'activityName': '',
						'phone': '',
						'activityId': this.activityId
					})
				}).then(({
					data
				}) => {
					console.log('', data)
					this.tableDataLoadingB = false
					if (data.code == 0) {
						let returnData = data.data
						this.baomingData = returnData
					} else {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: data.msg,
							type: 'warning'
						});
					}
				})
			},
			choideleteDe(row) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('activityPhoto/deleteActivityPhoto'),
						method: 'post',
						params: this.$http.adornParams({
							'activityPhotoId': row.activityPhotoId
						})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '删除成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.xiangceSelect()
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
			// 聊天内容
			complainDetails(row) {
				this.pageB = 1
				this.activityId = row.activityId
				this.neirongSelect(this.activityId)
				this.dialogFormVisible = true
			},
			select1() {
				this.pageB = 1
				this.neirongSelect(this.activityId)
			},
			// 重置
			cleans1() {
				this.content = ''
				this.pageB = 1
				this.neirongSelect(this.activityId)
			},
			// 获取聊天内容列表
			neirongSelect(activityId) {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('activityChat/selectChatContent'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.pageB,
						'limit': this.limitB,
						'activityId': this.activityId,
						'content': this.content
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.userData = returnData
				})
			},
			//删除聊天
			deleteuser(row) {
				// let delid = row.userId
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(
							'activityChat/deleteChatContentById?activityChatContentId=' + row
							.activityChatContentId),
						method: 'post',
						params: this.$http.adornData({})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '删除成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.neirongSelect(this.activityId)
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
				})
			},
		},
		mounted() {
			this.InformationSelect()
			this.homeSelect()
		}
	}
</script>

<style scoped>
.ongoing-activities-page__filter-input {
	width: 200px;
}

.ongoing-activities-page__filter-actions .el-button + .el-button {
	margin-left: 12px;
}

.ongoing-activities-page__link {
	color: #5cc477;
	cursor: pointer;
}

.ongoing-activities-page__link:hover {
	color: #3fae5c;
}

.ongoing-activities-page__pagination {
	display: flex;
	justify-content: flex-end;
	padding-top: 12px;
}

.nf-table-card .el-button + .el-button {
	margin-left: 0 !important;
	margin-top: 5px !important;
}
</style>

<style>
	.customWidth {
		width: 80% !important;
	}

	.adver_main.box {
		display: block;
		max-width: 100%;
		text-align: center;
		border: 1px dotted rgba(67, 79, 103, .4);
	}

	.cards {
		padding: 0 8px;
		margin-bottom: 15px;
	}

	.adver_main.box a {
		display: flex;
		justify-content: center;
		height: 150px;
		line-height: 150px;
		text-decoration: none
	}

	.bannerManin {
		border: 1px solid #e8e8e8;
		font-size: 14px;
		padding: 0 24px;
		display: flex;
		justify-content: center;
		align-items: center;
		height: 113px;
		color: rgba(0, 0, 0, .65);
	}

	.bannerManin span {
		display: inline-block;
		margin-left: 5px;
	}

	.bannerManin img {
		width: 48px;
		height: 48px;
		border-radius: 50%;
	}

	.bannerbtn {
		display: flex;
		border-top: none !important;
		border: 1px solid #e8e8e8;
		padding: 11px;
		font-size: 14px;
		color: #5cc477;
	}

	.bannerbtn a {
		flex: 1;
		text-align: center;
		color: #5cc477 !important;
		text-decoration: none;
	}

	.imgs {
		position: relative;
		border-radius: 6px;
		width: 148px;
		height: 148px;
		margin-right: 10px;
		display: inline-block;
	}

	.dels {
		position: absolute;
		top: 0;
		left: 0;
		display: none;
	}

	.dels .el-icon-delete {
		line-height: 148px;
		padding-left: 58px;
		font-size: 25px;
		color: #fff;
	}

	.imgs:hover .dels {
		width: 100%;
		height: 100%;
		background: #000;
		display: block;
		opacity: 0.5;
	}

	.bqList {
		padding: 4px 14px;
		margin: 4px;
		border: 1px solid #efefef;
		font-size: 12px;
		color: #999;
		border-radius: 4px;
		margin-right: 15px;
	}

	.delss {
		display: none;
		position: relative;
	}

	.delss .el-icon-delete {
		position: absolute;
		top: 0;
	}

	.bqList:hover .delss {
		display: initial;
		opacity: 0.5;

	}

	.tj {
		padding: 6px !important;
		margin: 4px;
		font-size: 12px;
		border: 1px solid #ccc;
		border-radius: 4px;
	}

	.biaoqianCla {
		position: relative;
		width: 90px;
		height: 75px;
		display: inline-block;
		padding: 10px;
		box-shadow: 0px 0px 7px #999;
		margin: 5px;
	}
</style>