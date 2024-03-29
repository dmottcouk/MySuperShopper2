package uk.co.dmott.mysupershopper2.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * Created by david on 22/03/18.
 */

@Entity
public class ShopItem implements Parcelable {

    @PrimaryKey
    @NonNull
    private String itemId;
    private String shopItemName;
    private String shopName;
    private int shopItemType;

    public ShopItem(String itemId, String shopItemName, String shopName, int shopItemType) {
        this.itemId = itemId;
        this.shopItemName = shopItemName;
        this.shopName = shopName;
        this.shopItemType = shopItemType;
    }

    //this was colorResource
    public int getShopItemType() {
        return shopItemType;
    }

    public void setShopItemType(int shopItemType) {
        this.shopItemType = shopItemType;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopItemName() {
        return shopItemName;
    }

    public void setShopItemName(String shopItemName) {
        this.shopItemName = shopItemName;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(itemId);
        dest.writeString(shopItemName);
        dest.writeString(shopName);
        dest.writeInt(shopItemType);

    }
    public static final Parcelable.Creator<ShopItem> CREATOR = new Parcelable.Creator<ShopItem>() {
        public ShopItem createFromParcel(Parcel in) {
            return new ShopItem(in);
        }
        @Override
        public ShopItem[] newArray(int size) {
            return new ShopItem[size];
        }
    };

    private ShopItem(Parcel in) {
        itemId = in.readString();
        shopItemName = in.readString();
        shopName = in.readString();
        shopItemType = in.readInt();

    }



}
