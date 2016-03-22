package serverNetty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Created by neikila.
 */
public class Server {
    private int port;
    private MessageCallback callback;
    private ChannelFuture f;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    public Server(int port, MessageCallback callback) {
        this.port = port;
        this.callback = callback;
    }

    public void run() {
        bossGroup = new NioEventLoopGroup(); // (1)
        workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap(); // (2)
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) // (3)
                    .childHandler(new ChannelInitializer<SocketChannel>() { // (4)

                        @Override
                        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                            super.exceptionCaught(ctx, cause);
                            System.out.println("Exception caught");
                        }

                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            System.out.println("Testing add");
                            ch.pipeline().addLast(new MessageHandler(callback));
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)          // (5)
                    .childOption(ChannelOption.SO_KEEPALIVE, true); // (6)

            System.out.println("Start on port: " + port);

            // Bind and start to accept incoming connections.
            f = b.bind(port).sync(); // (7)
            f.channel().closeFuture().sync();
            System.out.println("Stopped");
        } catch (InterruptedException interupted) {
            System.out.println("Interrupted");
            // Log.e("NettyServer", interupted.getLocalizedMessage());
        }
    }

    public void stop() {
        try {
            f.channel().close().sync();
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        } catch (InterruptedException exc) {
//            Log.e("NettyServer", exc.getLocalizedMessage());
        }
    }
}
