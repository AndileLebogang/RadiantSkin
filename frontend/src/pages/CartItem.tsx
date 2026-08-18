import type { CartLineItem } from "./Cart";

interface CartItemProps extends CartLineItem {
    onIncrease: (productId: number) => void;
    onDecrease: (productId: number) => void;
    onRemove: (productId: number) => void;
}

function CartItem({
    productId,
    name,
    price,
    imageUrl,
    quantity,
    onIncrease,
    onDecrease,
    onRemove,
}: CartItemProps) {
    return (
        <div className="cart-item">
            <img src={imageUrl} alt={name} />

            <div className="cart-item-info">
                <h4>{name}</h4>
                <div className="product-price">R{price}</div>
                </div>

                <div className="qty-control">
                    <button
                    onClick={() => onDecrease(productId)}
                    aria-label={quantity <= 1 ? 'Remove ${name}' : 'Decrease quantity of ${name}'}
                    >
                        -
                    </button>
                    <span>{quantity}</span>
                    <button onClick={() => onIncrease(productId)} aria-label={'Increase quantity of ${name}'}>
                        +
                    </button>
                </div>

                <button className="icon-button" onClick={() => onRemove(productId)} aria-label={'Remove ${name} from cart'}>
                    </button>
                    </div>
    );
}

export default CartItem;