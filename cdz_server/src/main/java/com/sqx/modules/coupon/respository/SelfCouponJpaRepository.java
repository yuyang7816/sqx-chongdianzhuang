package com.sqx.modules.coupon.respository;


import com.sqx.modules.coupon.entity.SelfCoupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SelfCouponJpaRepository extends JpaRepository<SelfCoupon, Long> {

    //分页查询
    Page<SelfCoupon> findAll(Pageable pageable);

    //条件查询
    Page<SelfCoupon> findAll(Specification<SelfCoupon> specification, Pageable pageable);

    @Query(value = "select * from self_coupon s where s.type=:type " +
            "and if(:couponName!='',coupon_name like concat('%',:couponName,'%'),1=1) " +
            "and if(:status!='',status=:status,1=1) " +
            "order by s.coupon_id ",nativeQuery = true)
    Page<SelfCoupon> findAll(Pageable pageable, @Param("type") Integer type,@Param("couponName") String couponName,@Param("status") String status);

    @Query(value = "select count(*) from self_coupon where coupon_name=:couponName",nativeQuery = true)
    Integer selectCouponNameCount(@Param("couponName") String couponName);

}
