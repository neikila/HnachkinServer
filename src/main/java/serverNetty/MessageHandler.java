package serverNetty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by neikila.
 */
public class MessageHandler extends ChannelInboundHandlerAdapter {
    private MessageCallback callback;

    public MessageHandler(MessageCallback callback) {
        this.callback = callback;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        System.out.println("Channel registered");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf in = (ByteBuf) msg;
        ByteBuf copyByteBuf = ((ByteBuf) msg).copy();
        ChannelFuture f = ctx.writeAndFlush(copyByteBuf);
        try {
            StringBuilder income = new StringBuilder();
            while (in.isReadable()) {
                income.append((char) in.readByte());
            }
            callback.apply(income);
            if (income.toString().startsWith("Quit")) {
                f.addListener(ChannelFutureListener.CLOSE);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.print("Error");
        } finally {
//            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
        System.out.println("Channel unregistered");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        // Close the connection when an exception is raised.
        System.out.println("Exception");
        cause.printStackTrace();
        ctx.close();
    }
}