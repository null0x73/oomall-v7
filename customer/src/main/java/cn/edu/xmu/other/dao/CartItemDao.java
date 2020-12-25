package cn.edu.xmu.other.dao;

import cn.edu.xmu.other.mapper.CartItemPoMapper;
import cn.edu.xmu.other.model.po.CartItemPo;
import cn.edu.xmu.other.model.po.CartItemPoExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class CartItemDao {

    @Autowired
    CartItemPoMapper cartItemPoMapper;


    public boolean isCartItemExistByCartItemId(Long cartItemId){
        return cartItemPoMapper.selectByPrimaryKey(cartItemId)!=null?true:false;
    }


    public boolean isCartItemBelongToCustomer(Long customerId, Long cartItemId){
        return cartItemPoMapper.selectByPrimaryKey(cartItemId).getCustomerId().equals(customerId);
    }


    public CartItemPo getCartItemByCartItemId(Long cartItemId){
        return cartItemPoMapper.selectByPrimaryKey(cartItemId);
    }


    public List<CartItemPo> getShoppingCartInListByCustomerId(Long customerId){
        CartItemPoExample example = new CartItemPoExample();
        example.createCriteria().andCustomerIdEqualTo(customerId);
        List<CartItemPo> cartItemPos = cartItemPoMapper.selectByExample(example);
        return cartItemPos;
    }


    public PageInfo<CartItemPo> getShoppingCartInPageByCustomerId(Long customerId, Integer pageIndex, Integer pageSize) {

        CartItemPoExample example = new CartItemPoExample();
        example.createCriteria().andCustomerIdEqualTo(customerId);

        PageHelper.startPage(pageIndex, pageSize);
        PageInfo<CartItemPo> pageResult = new PageInfo<>(cartItemPoMapper.selectByExample(example));

        return pageResult;

    }

    public CartItemPo addItemToShoppingCart(Long customerId, Long goodsSkuId, Integer quantity, Long singlePrice) {

        CartItemPo createPo = new CartItemPo();
        createPo.setCustomerId(customerId);
        createPo.setGoodsSkuId(goodsSkuId);
        createPo.setQuantity(quantity);
        createPo.setPrice(singlePrice);
        createPo.setGmtCreate(LocalDateTime.now());
        createPo.setGmtModified(LocalDateTime.now());

        cartItemPoMapper.insert(createPo);

        return createPo;

    }

    public Integer clearShoppingCart(Long customerId) {

        CartItemPoExample example = new CartItemPoExample();
        example.createCriteria().andCustomerIdEqualTo(customerId);

        return cartItemPoMapper.deleteByExample(example);

    }

    public Integer deleteCartItem(Long customerId, Long cartItemId) {

        CartItemPoExample example = new CartItemPoExample();
        example.createCriteria().andIdEqualTo(cartItemId);

        return cartItemPoMapper.deleteByExample(example);

    }

    public Integer modifyCartItem(Long cartItemId, Long goodsSkuId, Integer quantity, Long price) {

        CartItemPo updatePo = new CartItemPo();
        updatePo.setId(cartItemId);
        updatePo.setGoodsSkuId(goodsSkuId);
        updatePo.setQuantity(quantity);
        updatePo.setPrice(price);

        return cartItemPoMapper.updateByPrimaryKeySelective(updatePo);

    }

}
