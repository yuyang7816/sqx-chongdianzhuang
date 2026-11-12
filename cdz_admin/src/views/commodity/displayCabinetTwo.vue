<template>
  <div class="nf-page display-cabinet-two-page">
    <div class="nf-page-hero">
      <span class="nf-page-hero__badge">展示柜管理</span>
      <h2 class="nf-page-hero__title">展示柜设备</h2>
      <p class="nf-page-hero__desc">管理大厅展示柜设备与台球馆展示柜，支持按名称、货物状态及台球馆信息筛选，可添加、修改、删除设备及批量下载二维码。</p>
      <div class="nf-page-hero__bar"></div>
    </div>

    <div class="nf-filter-card" v-show="activeName === 'first'">
      <div class="nf-filter-card__head">
        <i class="el-icon-search nf-filter-card__head-icon"></i>
        <span>筛选条件</span>
      </div>
      <el-form :inline="true" class="nf-filter-card__body" @submit.native.prevent>
        <el-form-item label="名称">
          <el-input
            class="display-cabinet-two-page__filter-input"
            @keydown.enter.native="refresh1"
            clearable
            placeholder="请输入名称"
            v-model="nameT">
          </el-input>
        </el-form-item>
        <el-form-item label="是否有货物">
          <el-select
            class="display-cabinet-two-page__filter-select"
            v-model="isEmptyT"
            placeholder="请选择是否有货物"
            @change="refresh1()">
            <el-option v-for="item in isEmptyList" :key="item.innerCode" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item class="display-cabinet-two-page__filter-actions">
          <el-button type="primary" icon="el-icon-search" @click="refresh1">查询</el-button>
          <el-button type="primary" icon="el-icon-refresh-left" @click="clear">重置</el-button>
          <el-button :disabled="!isAuth('displayCabinet:add')" type="primary" icon="document"
            @click="addCouponSp(1)">添加</el-button>
          <el-button type="primary" icon="document" @click="transferClcik(1)" :disabled="checkBoxData.length <= 0">
            批量下载二维码
          </el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="nf-filter-card" v-show="activeName === 'second'">
      <div class="nf-filter-card__head">
        <i class="el-icon-search nf-filter-card__head-icon"></i>
        <span>筛选条件</span>
      </div>
      <el-form :inline="true" class="nf-filter-card__body" @submit.native.prevent>
        <el-form-item label="台球馆名称">
          <el-input
            class="display-cabinet-two-page__filter-input"
            @keydown.enter.native="select"
            clearable
            placeholder="请输入台球馆名称"
            v-model="title">
          </el-input>
        </el-form-item>
        <el-form-item label="台球馆状态">
          <el-select
            class="display-cabinet-two-page__filter-select"
            v-model="status"
            placeholder="请选择台球馆状态"
            @change="select()">
            <el-option v-for="item in statusmain" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item class="display-cabinet-two-page__filter-actions">
          <el-button type="primary" icon="el-icon-search" @click="select">查询</el-button>
          <el-button type="primary" icon="el-icon-refresh-left" @click="cleans">重置</el-button>
          <el-button type="primary" icon="document" @click="transferClcik(2)" :disabled="checkBoxData.length <= 0">
            批量下载二维码
          </el-button>
        </el-form-item>
      </el-form>
    </div>

    <el-tabs v-model="activeName" @tab-click="handleClick" class="display-cabinet-two-page__tabs">
      <el-tab-pane label="大厅展示柜" name="first">
        <div class="nf-table-card">
        <el-table v-loading="tableDataLoading" :data="shangpinData.list" @selection-change="changeFun">
          <el-table-column type="selection">
          </el-table-column>
          <el-table-column prop="id" label="编号" width="80">
          </el-table-column>
          <el-table-column prop="tearoomName" label="台球馆名称">
          </el-table-column>
          <el-table-column prop="name" label="名称">
          </el-table-column>
          <el-table-column prop="hardwareName" label="硬件名称">
          </el-table-column>
          <el-table-column prop="hardwareId" label="硬件编号" width="100">
          </el-table-column>
          <el-table-column prop="equipmentSn" label="指令编号" width="100">
          </el-table-column>
          <el-table-column prop="equipmentType" label="指令类型" width="100">
          </el-table-column>
          <el-table-column prop="instructCode" label="弹射器编号" width="100">
          </el-table-column>
          <el-table-column prop="isEmpty" label="是否有货物" width="100">
            <template slot-scope="scope">
              <span v-if="scope.row.isEmpty==1">有</span>
              <span v-else>无</span>
            </template>
          </el-table-column>
          <el-table-column label="二维码" width="100">
            <template slot-scope="scope">
              <el-button size="mini" type="primary" @click="couponQr(scope.row,1)" style="margin: 5px;">查看
              </el-button>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="创建时间" width="150">
          </el-table-column>
          <el-table-column label="操作" width="150">
            <template slot-scope="scope">
              <el-button size="mini" type="primary" :disabled="!isAuth('displayCabinet:update')"
                @click="addCouponSp(scope.row)">修改
              </el-button>
              <el-button size="mini" type="danger" :disabled="!isAuth('displayCabinet:delete')"
                @click="couponDeletesSp(scope.row)">
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <div class="display-cabinet-two-page__pagination">
          <el-pagination @size-change="handleSizeChangeSp" @current-change="handleCurrentChangeSp"
            :page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
            layout="total,sizes, prev, pager, next" :total="shangpinData.totalCount">
          </el-pagination>
        </div>
        </div>
      </el-tab-pane>
      <el-tab-pane label="台球馆列表" name="second">
        <div class="nf-table-card">
        <el-table v-loading="tableDataLoading" :data="shopData.list" @selection-change="changeFun">
          <el-table-column type="selection">
          </el-table-column>
          <el-table-column fixed prop="goodsId" label="编号" width="80">
          </el-table-column>
          <el-table-column fixed prop="goodsCoverImage" label="台球馆图片">
            <template slot-scope="scope">
              <img :src="scope.row.goodsCoverImage" alt="" width="60" height="60">
            </template>
          </el-table-column>
          <el-table-column prop="goodsName" label="台球馆名称" width="200">
          </el-table-column>
          <el-table-column prop="price" label="台球馆小时价">
            <template slot-scope="scope">
              <span style="color: #f56c6c;">{{scope.row.price?scope.row.price:0}}</span>
            </template>
          </el-table-column>
          <el-table-column prop="goodsDetails" label="台球馆详情" width="250">
          </el-table-column>
          <el-table-column prop="endTime" label="套餐有效时间(天)">
            <template slot-scope="scope">
              {{scope.row.endTime}}天
            </template>
          </el-table-column>
          <el-table-column prop="goodsType" label="台球馆规格">
          </el-table-column>
          <el-table-column prop="sales" label="台球馆销量">
            <template slot-scope="scope">
              <span>{{scope.row.sales ? scope.row.sales : '0'}}</span>
            </template>
          </el-table-column>
          <el-table-column label="二维码" width="100">
            <template slot-scope="scope">
              <el-button size="mini" type="primary" @click="couponQr(scope.row,2)" style="margin: 5px;">查看
              </el-button>
            </template>
          </el-table-column>
          <el-table-column prop="roomNo" label="房间号码">
          </el-table-column>
          <el-table-column prop="roomWifi" label="wiFi密码">
          </el-table-column>
          <el-table-column prop="goodsTips" label="提示信息" width="200">
          </el-table-column>
          <el-table-column prop="createTime" label="创建时间" width="160">
          </el-table-column>

          <el-table-column fixed="right" prop="status" label="台球馆状态">
            <template slot-scope="scope">
              <span class="display-cabinet-two-page__status-on" v-if="scope.row.status == 1 ">上架</span>
              <span class="display-cabinet-two-page__status-pause" v-else-if="scope.row.status == 2 ">暂停使用</span>
              <span class="display-cabinet-two-page__status-clean" v-else-if="scope.row.status == 3 ">清洁中</span>
              <span class="display-cabinet-two-page__status-maintain" v-else-if="scope.row.status == 4 ">维护中</span>
              <span class="display-cabinet-two-page__status-closed" v-else-if="scope.row.status == 5 ">已打烊</span>
            </template>
          </el-table-column>
          <el-table-column fixed="right" label="操作" width="120" style="line-height: 33px;">
            <template slot-scope="scope">
              <el-button size="mini" type="primary" :disabled="!isAuth('displayCabinet:update')"
                @click="zhanshiguiBtn(scope.row)">展示柜列表
              </el-button>

            </template>
          </el-table-column>
        </el-table>
        <div class="display-cabinet-two-page__pagination">
          <el-pagination @size-change="handleSizeChange1" @current-change="handleCurrentChange1"
            :page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
            layout="total,sizes, prev, pager, next" :total="shopData.totalCount">
          </el-pagination>
        </div>
        </div>
      </el-tab-pane>
    </el-tabs>

      <el-dialog :title="titles" :visible.sync="dialogFormVisibleSp" center>
        <!-- <div style="margin-bottom: 10px;">
          <span style="width: 200px;display: inline-block;text-align: right;">台球馆：</span>
          <el-input style="width:50%;" v-model="tearoomName" @focus="terroomBtn()" placeholder="请选择台球馆"></el-input>
        </div> -->
        <div style="margin-bottom: 10px;">
          <span style="width: 200px;display: inline-block;text-align: right;">名称：</span>
          <el-input style="width:50%;" v-model="name" placeholder="请输入名称"></el-input>
        </div>
        <div style="margin-bottom: 10px;">
          <span style="width: 200px;display: inline-block;text-align: right;">设备组：</span>
          <el-select v-model="hardwareId" placeholder="请选择设备" style="width:50%;" @change="shebeiBtn()">
            <el-option v-for="item in bianhaoList1" :key="item.innerCode" :label="item.vmsGroupName"
              :value="item.innerCode">
            </el-option>
          </el-select>
        </div>
        <div style="margin-bottom: 10px;">
          <span style="width: 200px;display: inline-block;text-align: right;">设备：</span>
          <el-select v-model="equipmentSn" placeholder="请选择设备" style="width:50%;" @change="shebeiBtn2()">
            <el-option v-for="item in bianhaoList" :key="item.vmsDeviceNo" :label="item.vmsDeviceTypeCh"
              :value="item.vmsDeviceNo">
            </el-option>
          </el-select>
        </div>
        <div style="margin-bottom: 10px;">
          <span style="width: 200px;display: inline-block;text-align: right;">弹射器：</span>
          <el-select v-model="switchDisplay" placeholder="请选择弹射器" style="width:50%;" @change="shebeiBtn3()">
            <el-option v-for="item in switchList" :key="item.instructCode" :label="item.switchDisplay"
              :value="item.instructCode">
            </el-option>
          </el-select>
        </div>
        <div style="margin-bottom: 10px;">
          <span style="width: 200px;display: inline-block;text-align: right;">是否有货物：</span>
          <el-radio-group v-model="isEmpty">
            <el-radio :label="1">有</el-radio>
            <el-radio :label="2">无</el-radio>
          </el-radio-group>
        </div>
        <div style="margin-bottom: 10px;">
          <span style="width: 200px;display: inline-block;text-align: right;">商品：</span>
          <el-select v-model="smallGoodsId" style="width:50%;" filterable>
            <el-option v-for="item in statesnum" :key="item.id" :label="item.title" :value="item.id">
            </el-option>
          </el-select>
        </div>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisibleSp = false">取 消</el-button>
          <el-button type="primary" @click="couponNoticeToSp()">确 定</el-button>
        </div>
      </el-dialog>
      <!-- 店铺二维码 -->
      <el-dialog :visible.sync="dialogFormVisible2" center title="二维码">
        <div>
          <img :src="erweimaImg" style="width: 50%;margin-left: 25%;" />
          <div style="text-align: center;margin-top: 20px;">
            <el-button type="primary" @click="chakan()">打开窗口查看</el-button>
            <el-button type="primary" @click="downloadImage()"><a style="color: #fff;">下载</a></el-button>

          </div>
        </div>
      </el-dialog>
      <!-- 商品数据 -->
      <el-dialog :visible.sync="dialogFormVisibleGoods" center title="商品列表">
      </el-dialog>
      <!-- 台球馆数据 -->
      <el-dialog :visible.sync="dialogFormVisibleTre" center title="台球馆列表">
        <div style="margin:2% 0;display: inline-block;">
          <span>台球馆名称：</span>
          <el-input style="width: 160px;" @keydown.enter.native="selectList" clearable placeholder="请输入台球馆名称"
            v-model="storeNameT"></el-input>&nbsp;&nbsp;&nbsp;&nbsp;
        </div>
        <div style="display: inline-block;">
          <el-button style='margin-left:15px;' size="mini" type="primary" icon="document"
            @click="selectList">查询</el-button>
        </div>
        <el-table v-loading="tableDataLoading" :data="tableDataTer.list">
          <el-table-column fixed="left" prop="tearoomId" label="编号" width="80">
          </el-table-column>
          <el-table-column prop="tearoomName" fixed="left" label="台球馆名称" width="120">
          </el-table-column>
          <el-table-column prop="tearoomTitleImage" label="台球馆logo">
            <template slot-scope="scope">
              <span v-if="scope.row.tearoomTitleImage == null">暂无图片</span>
              <img v-else :src="scope.row.tearoomTitleImage" alt="" width="40" height="40">
            </template>
          </el-table-column>
          <el-table-column prop="tearoomCoverImage" label="台球馆轮播图" width="200">
            <template slot-scope="scope">
              <div v-if="scope.row.tearoomCoverImage">
                <span v-for="(item,index) in scope.row.tearoomCoverImage.split(',')">
                  <el-popover placement="top-start" title="" trigger="hover">
                    <img style="width: 50px; height: 50px;margin: 3px;" :src="item" alt="" slot="reference">
                    <img style="width: 300px; height: 300px" :src="item" alt="">
                  </el-popover>
                </span>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="store_address" label="台球馆地址" width="200">
            <template slot-scope="scope">
              <div>
                {{scope.row.tearoomProvince}}{{scope.row.tearoomCity}}{{scope.row.tearoomDistrict}}{{scope.row.tearoomAddress}}
              </div>
              <div>经度：{{scope.row.tearoomLongitude ? scope.row.tearoomLongitude : '未绑定'}}</div>
              <div>纬度：{{scope.row.tearoomLatitude ? scope.row.tearoomLatitude : '未绑定'}}</div>
            </template>
          </el-table-column>
          <el-table-column prop="startTime" label="营业时间" width="100">
            <template slot-scope="scope">
              <div>{{scope.row.startTime}}--{{scope.row.endTime}}</div>
            </template>
          </el-table-column>
          <el-table-column prop="tearoomLabel" label="台球馆标签">
          </el-table-column>

          <el-table-column prop="score" label="台球馆评分">
          </el-table-column>

          <el-table-column label="操作" width="100" fixed="right">
            <template slot-scope="scope">
              <el-button size="mini" type="primary" @click="querenBtnTer(scope.row)">选择
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <div style="text-align: center;margin-top: 10px;">
          <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
            :page-sizes="[10, 20, 30, 50, 100]" :page-size="limit1" :current-page="page1"
            layout="total,sizes, prev, pager, next" :total="tableDataTer.totalCount">
          </el-pagination>
        </div>
      </el-dialog>
      <!-- 展示柜列表 -->
      <el-dialog :visible.sync="dialogFormVisibleZsg" center title="展示柜列表" width="80%">
        <div style="margin:5px;display: inline-block;">
          <span>名称：</span>
          <el-input style="width: 160px;" @keydown.enter.native="refresh1" clearable placeholder="请输入名称"
            v-model="nameT"></el-input>&nbsp;&nbsp;&nbsp;&nbsp;
        </div>
        <div style="margin:5px;display: inline-block;">
          <span>是否有货物：</span>
          <el-select v-model="isEmptyT" placeholder="请选择是否有货物" style="width:150px;" @change="refresh1()">
            <el-option v-for="item in isEmptyList" :key="item.innerCode" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </div>
        <div style="display: inline-block;">
          <el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="refresh1">查询
          </el-button>
          <el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="clear">重置
          </el-button>
          <el-button style='margin-left:15px;' :disabled="!isAuth('displayCabinet:add')" size="mini" type="primary"
            icon="document" @click="addCouponSp(1)">添加</el-button>
          <el-button style='margin:0 0 20px 20px;' size="mini" type="primary" icon="document"
          	@click="transferClcik(3)" :disabled="checkBoxData.length <= 0 ">
          	批量下载二维码
          </el-button>
        </div>
        <el-table v-loading="tableDataLoading" :data="shangpinData.list" @selection-change="changeFun">
          <el-table-column type="selection">
          </el-table-column>
          <el-table-column prop="id" label="编号" width="80">
          </el-table-column>
          <el-table-column prop="title" label="名称">
          </el-table-column>

          <el-table-column prop="hardwareName" label="硬件名称">
          </el-table-column>
          <el-table-column prop="hardwareId" label="硬件编号" width="100">
          </el-table-column>
          <el-table-column prop="equipmentSn" label="指令编号" width="100">
          </el-table-column>
          <el-table-column prop="equipmentType" label="指令类型" width="100">
          </el-table-column>
          <el-table-column prop="instructCode" label="弹射器编号" width="100">
          </el-table-column>
          <el-table-column prop="isEmpty" label="是否有货物" width="100">
            <template slot-scope="scope">
              <span v-if="scope.row.isEmpty==1">有</span>
              <span v-else>无</span>
            </template>
          </el-table-column>
          <el-table-column label="二维码" width="100">
            <template slot-scope="scope">
              <el-button size="mini" type="primary" @click="couponQr(scope.row,3)" style="margin: 5px;">查看
              </el-button>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="创建时间" width="150">
          </el-table-column>
          <el-table-column label="操作" width="150">
            <template slot-scope="scope">
              <el-button size="mini" type="primary" :disabled="!isAuth('displayCabinet:update')"
                @click="addCouponSp(scope.row)">修改
              </el-button>
              <el-button size="mini" type="danger" :disabled="!isAuth('displayCabinet:delete')"
                @click="couponDeletesSp(scope.row)">
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <div style="text-align: center;margin-top: 10px;">
          <el-pagination @size-change="handleSizeChangeSp" @current-change="handleCurrentChangeSp"
            :page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
            layout="total,sizes, prev, pager, next" :total="shangpinData.totalCount">
          </el-pagination>
        </div>
      </el-dialog>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        page: 1,
        size: 10,
        shangpinData: {},
        titles: '添加',
        image: '',
        id: '',
        name: '', //展示柜名称
        equipmentSn: '', //指令Id
        equipmentType: '', //指令类型
        hardwareName: '', //硬件名称
        hardwareId: '', //硬件编号
        isEmpty: '', //是否有货物 1有 2无
        smallGoodsId: '', //商品id
        switchDisplay: '', //弹射器名称
        instructCode: '', //弹射器编号


        dialogFormVisibleSp: false,
        tableDataLoading: false,
        userId: '',
        tearoomId: '',
        tearoomName: '',
        statesnum: [],

        innerCode: '',
        bianhaoList1: [],
        bianhaoList: [],
        switchList: [],
        dialogFormVisible2: false,
        erweimaImg: '',
        erweimaImgName:'',
        shearUrl: '',
        dialogFormVisibleGoods: false,
        dialogFormVisibleTre: false,
        tableDataTer: {},
        page1: 1,
        limit1: 10,
        storeNameT: '',
        isEmptyList: [{
            value: '',
            label: '全部'
          },
          {
            value: 1,
            label: '有'
          },
          {
            value: 2,
            label: '无'
          },
        ],
        tearoomNameT: '',
        nameT: '',
        isEmptyT: '',
        activeName: 'first',
        equipment_sn: '',
        merchantId: '',
        storeId: '',
        shopData: {},
        title: '',
        status: 0,
        statusmain: [{
            value: 0,
            label: '全部'
          },
          {
            value: 1,
            label: '上架'
          },
          {
            value: 2,
            label: '暂停使用'
          },
          {
            value: 3,
            label: '清洁中'
          },
          {
            value: 4,
            label: '维护中'
          },
          {
            value: 5,
            label: '已打烊'
          }
        ],
        dialogFormVisibleZsg: false,
        goodsId: '',
        checkBoxData: [], //多选框选择的值
        types:1,
      }
    },
    methods: {
      // tabs切换
      handleClick(tab, event) {
        this.page = 1
        this.goodsId = ''
        this.nameT = ''
        this.isEmptyT = ''

        if (tab._props.label == '大厅展示柜') {
          this.types = 1
          this.shangpinSelect()
        }
        if (tab._props.label == '台球馆列表') {
          this.types = 2
          this.shopSelect()
        }
      },
      handleSizeChange1(val) {
        this.size = val
        this.shopSelect()
      },
      handleCurrentChange1(val) {
        this.page = val
        this.shopSelect()
      },
      handleSizeChangeSp(val) {
        this.size = val
        this.shangpinSelect()
      },
      handleCurrentChangeSp(val) {
        this.page = val
        this.shangpinSelect()
      },
      handleSizeChange(val) {
        this.limit1 = val
        this.dataSelectTer()
      },
      handleCurrentChange(val) {
        this.page1 = val
        this.dataSelectTer()
      },
      // 刷新
      refresh1() {
        this.page = 1
        this.shangpinSelect()
        this.shangpinSelectCl()
      },
      clear() {
        this.page = 1
        this.tearoomNameT = ''
        this.nameT = ''
        this.isEmptyT = ''
        this.shangpinSelect()
      },
      // 展示柜管理
      shangpinSelect() {
        this.tableDataLoading = true
        this.$http({
          url: this.$http.adornUrl('smallHardware/selectSmallHardwareList'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.page,
            'limit': this.size,
            'isEmpty': this.isEmptyT,
            'name': '',
            'title': this.nameT,
            'tearoomName': this.tearoomNameT,
            'tearoomId': this.storeId,
            'goodsId': this.goodsId,
          })
        }).then(({
          data
        }) => {
          this.tableDataLoading = false
          let returnData = data.data;
          this.shangpinData = returnData
        })
      },
      // 添加商品
      addCouponSp(row, index) {
        console.log('row```', row)
        this.shebeiSelect1()
        this.shangpinSelectCl()
        if (row != 1) {
          this.titles = '修改'

          this.id = row.id
          this.name = row.name
          this.equipmentSn = row.equipmentSn
          this.equipmentType = row.equipmentType
          this.hardwareName = row.hardwareName
          this.hardwareId = row.hardwareId
          this.isEmpty = row.isEmpty
          this.smallGoodsId = row.smallGoodsId
          // this.tearoomId = row.tearoomId
          this.tearoomName = row.tearoomName
          this.switchDisplay = row.instructCode
          this.instructCode = row.instructCode
          this.shebeiSelect()
          this.dialogFormVisibleSp = true
        } else {
          this.titles = '添加'
          this.id = ''
          this.name = ''
          this.equipmentSn = ''
          this.equipmentType = ''
          this.hardwareName = ''
          this.hardwareId = ''
          this.isEmpty = ''
          this.smallGoodsId = ''
          this.tearoomName = ''
          this.switchDisplay = ''
          this.instructCode = ''
          this.dialogFormVisibleSp = true
        }

      },
      // 添加商品
      couponNoticeToSp() {

        // if (this.tearoomId == '' || this.tearoomName == null) {
        //   this.$notify({
        //     title: '提示',
        //     duration: 1800,
        //     message: '请选择台球馆',
        //     type: 'warning'
        //   });
        //   return
        // }

        if (this.name == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请输入名称',
            type: 'warning'
          });
          return
        }
        if (this.equipmentSn === '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请输入指令Id',
            type: 'warning'
          });
          return
        }
        if (this.instructCode === '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请选择弹射器',
            type: 'warning'
          });
          return
        }
        if (this.hardwareName == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请输入硬件名称',
            type: 'warning'
          });
          return
        }
        if (this.hardwareId == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请输入硬件编号',
            type: 'warning'
          });
          return
        }
        if (this.isEmpty == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请输入是否有货物',
            type: 'warning'
          });
          return
        }
        if (this.smallGoodsId == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请输入商品id',
            type: 'warning'
          });
          return
        }
        if (this.titles == '添加') {
          var url = 'smallHardware/insertSmallHardware'
        } else {
          var url = 'smallHardware/updateSmallHardware'
        }
        this.$http({
          url: this.$http.adornUrl(url),
          method: 'post',
          data: this.$http.adornData({
            'id': this.id,
            'name': this.name,
            'equipmentSn': this.equipmentSn,
            'equipmentType': this.equipmentType,
            'hardwareName': this.hardwareName,
            'hardwareId': this.hardwareId,
            'isEmpty': this.isEmpty,
            'smallGoodsId': this.smallGoodsId,
            'tearoomId': this.tearoomId,
            'instructCode': this.instructCode,
            'goodsId': this.goodsId,
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
                this.dialogFormVisibleSp = false
                this.shangpinSelect()
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
      },
      handleAvatarSuccess3(file, fileList) {
        console.log(file)
        this.image = file.data
      },
      // 删除商品
      couponDeletesSp(row) {
        this.$confirm(`确定删除此数据?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('smallHardware/deleteSmallHardware'),
            method: 'post',
            params: this.$http.adornParams({
              'id': row.id
            })
          }).then(({
            data
          }) => {
            if (data && data.code == 0) {
              this.$message({
                message: '删除成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  this.shangpinSelect()
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
      // 商品管理
      shangpinSelectCl() {
        this.$http({
          url: this.$http.adornUrl('smallGoods/selectSmallGoodsList'),
          method: 'get',
          params: this.$http.adornParams({
            'page': 1,
            'limit': 1000,
          })
        }).then(({
          data
        }) => {
          let returnData = data.data;
          this.statesnum = returnData.list
        })
      },
      // 获取设备组列表
      shebeiSelect1() {
        let storeId = this.$route.query.storeId
        this.$http({
          url: this.$http.adornUrl('room/selectGroup'),
          method: 'get',
          params: this.$http.adornParams({
            'innerCode': '',
          })
        }).then(({
          data
        }) => {
          let returnData = data.data.result;
          this.bianhaoList1 = returnData
        })
      },
      // 获取设备列表
      shebeiSelect() {
        let storeId = this.$route.query.storeId
        this.$http({
          url: this.$http.adornUrl('room/selectGroup'),
          method: 'get',
          params: this.$http.adornParams({
            'innerCode': this.hardwareId,
          })
        }).then(({
          data
        }) => {
          let returnData = data.data.result;
          var arrList = []
          if (returnData[0].devices) {
            for (var i in returnData[0].devices) {
              if (returnData[0].devices[i].vmsDeviceTypeCh == 'DTU' || returnData[0].devices[i].vmsDeviceTypeCh ==
                '核心控制器') {

              } else {
                arrList.push(returnData[0].devices[i])
              }
            }
            this.bianhaoList = arrList
          }
          if (this.hardwareName != '') {
            for (var i in this.bianhaoList) {
              if (this.hardwareName == this.bianhaoList[i].vmsDeviceTypeCh) {
                this.switchList = this.bianhaoList[i].vmsInstructs
              }
            }
          }
        })
      },
      shebeiBtn() {
        this.equipmentSn = ''
        this.shebeiSelect()

      },
      shebeiBtn2() {
        this.switchDisplay = ''
        this.instructCode = ''
        for (var i in this.bianhaoList) {
          if (this.equipmentSn == this.bianhaoList[i].vmsDeviceNo) {
            this.hardwareName = this.bianhaoList[i].vmsDeviceTypeCh
            this.switchList = this.bianhaoList[i].vmsInstructs
          }
        }
      },
      shebeiBtn3() {
        for (var i in this.switchList) {
          if (this.switchDisplay == this.switchList[i].instructCode) {
            this.instructCode = this.switchList[i].instructCode
          }
        }
      },
      // 二维码弹框
      couponQr(row,index) {
        this.erweimaImg = this.$http.adornUrl('invite/mpCreateQr?relation=' + row.id + '&page=my/cabinet/cabinet')
        if(index==1){
          this.erweimaImgName =row.tearoomName
        }else if(index==2){
          this.erweimaImgName =row.goodsName
        }else{
          this.erweimaImgName =row.title
        }

        this.dialogFormVisible2 = true
      },
      chakan() {
        window.open(this.erweimaImg, '_blank');
      },
      downloadImage(e) {
          let hreLocal = "";
          hreLocal = this.erweimaImg;
          this.downloadByBlob(hreLocal, this.erweimaImgName)

      },
      downloadByBlob(url, name) {
        let image = new Image()
        image.setAttribute('crossOrigin', 'anonymous')
        image.src = url
        image.onload = () => {
          let canvas = document.createElement('canvas')
          canvas.width = image.width
          canvas.height = image.height
          let ctx = canvas.getContext('2d')
          ctx.drawImage(image, 0, 0, image.width, image.height)
          canvas.toBlob((blob) => {
            let url = URL.createObjectURL(blob)
            this.download(url, name)
            // 用完释放URL对象
            URL.revokeObjectURL(url)
          })
        }
      },
      download(href, name) {
        let eleLink = document.createElement('a')
        eleLink.download = name
        eleLink.href = href
        eleLink.click()
        eleLink.remove()
        this.$message({
          message: '下载完成！',
          type: 'success',
          duration: 1500,
          onClose: () => {
          }
        })
        if(this.types==1){
          this.shangpinSelect()
        }
        if(this.types==2){
          this.shopSelect()
        }
        if(this.types==3){
          this.shangpinSelect()
        }

      },
      // 台球馆列表
      dataSelectTer() {
        this.tableDataLoading = true
        this.$http({
          url: this.$http.adornUrl('tearoom/selectTearoomList'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.page1,
            'limit': this.limit1,
            'tearoomName': this.storeNameT,
            // 'nickname': this.userNameT,
            'city': '',
            'status': '',

          })
        }).then(({
          data
        }) => {
          this.tableDataLoading = false
          let returnData = data.data
          this.tableDataTer = returnData
        })
      },
      selectList() {
        this.page1 = 1
        this.dataSelectTer()
      },
      terroomBtn() {
        this.page1 = 1
        this.storeNameT = ''
        this.dataSelectTer()
        this.dialogFormVisibleTre = true
      },
      querenBtnTer(row) {
        this.tearoomId = row.tearoomId
        this.tearoomName = row.tearoomName
        this.dialogFormVisibleTre = false
      },
      // 获取台球馆数据
      shopSelect() {
        let storeId = this.$route.query.storeId
        this.tableDataLoading = true
        this.$http({
          url: this.$http.adornUrl('goods/selectGoodsList'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.page,
            'limit': this.size,
            'goodsName': this.title,
            // 'goods_cate': this.typeId,
            'status': this.status,
            'tearoomId': storeId
          })
        }).then(({
          data
        }) => {
          this.tableDataLoading = false
          let returnData = data.data;
          this.shopData = returnData
          this.goods_list = returnData.list
          this.tableDataqqq = returnData
        })
      },
      //搜索
      select() {
        this.page = 1
        this.shopSelect()
      },
      // 重置
      cleans() {
        this.page = 1
        this.title = ''
        this.status = 0
        this.typeId = ''
        this.value = []
        this.shopSelect()
      },
      // 展示柜列表弹框
      zhanshiguiBtn(row) {
        this.types = 3
        this.goodsId = row.goodsId
        this.shangpinSelect()
        this.dialogFormVisibleZsg = true
      },
      // 多选
      changeFun(val) {
      	this.checkBoxData = val
      },
      // 批量下载
      transferClcik(index,id) {

      	var ids = id ? [id] : this.checkBoxData.map(item => {

      		return item.id
      	})
        var names =  this.checkBoxData.map(item => {
          if(index==1){
           return item.tearoomName
          }else if(index==2){
            return item.goodsName
          }else{
            return item.title
          }
        })

      	for (var i in ids) {

          this.erweimaImg = this.$http.adornUrl('invite/mpCreateQr?relation=' + ids[i] + '&page=my/cabinet/cabinet')
          this.erweimaImgName = names[i]
      		this.downloadImage()
        }
         this.checkBoxData = []
      },
    },
    mounted() {
      let activeName = this.$route.query.activeName
      this.equipment_sn = this.$route.query.equipment_sn
      this.merchantId = this.$route.query.merchantId
      this.storeId = this.$route.query.storeId
      this.tearoomId = this.$route.query.storeId
      this.shangpinSelect()
      this.shangpinSelectCl()
    }
  }
</script>

<style scoped>
.display-cabinet-two-page__filter-input {
  width: 160px;
}

.display-cabinet-two-page__filter-select {
  width: 150px;
}

.display-cabinet-two-page__filter-actions .el-button + .el-button {
  margin-left: 12px;
}

.display-cabinet-two-page__tabs {
  margin-bottom: 0;
}

.display-cabinet-two-page__tabs >>> .el-tabs__header {
  margin-bottom: 0;
}

.display-cabinet-two-page__tabs .nf-table-card {
  margin-bottom: 0;
}

.display-cabinet-two-page__tabs >>> .el-tabs__item.is-active {
  color: #5cc477;
}

.display-cabinet-two-page__tabs >>> .el-tabs__active-bar {
  background-color: #5cc477;
}

.display-cabinet-two-page__pagination {
  display: flex;
  justify-content: center;
  padding-top: 12px;
}

.display-cabinet-two-page__status-on {
  color: #13ce66;
  cursor: pointer;
}

.display-cabinet-two-page__status-pause {
  color: #ff4949;
  cursor: pointer;
}

.display-cabinet-two-page__status-clean {
  color: #5cc477;
  cursor: pointer;
}

.display-cabinet-two-page__status-maintain {
  color: orange;
  cursor: pointer;
}

.display-cabinet-two-page__status-closed {
  color: #999;
  cursor: pointer;
}

.nf-table-card .el-button + .el-button {
  margin-left: 0 !important;
  margin-top: 5px !important;
}
</style>
