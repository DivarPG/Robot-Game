//import game.ui.cell.CellItemWidget;
//import game.ui.cell.CellLayout;
//import game.ui.resource.ResourceProvider;
//import game.ui.resource.gif.GifResource;
//import game.ui.resource.image.ImageResource;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.image.BufferedImage;
//
//public class PortalWidget extends CellItemWidget {
//
//    public PortalWidget(ResourceProvider<ImageIcon, GifResource> gifProvider) {
//        super(null);
//
//        setLayout(new BorderLayout());
//
//        JLabel label = new JLabel(gifProvider.get(GifResource.PORTAL));
//        label.setHorizontalAlignment(SwingConstants.CENTER);
//
//        add(label, BorderLayout.CENTER);
//
//        setOpaque(false);
//        setPreferredSize(new Dimension(150, 150));
//    }
//
//    @Override
//    protected Dimension getDimension() {
//        return new Dimension(150, 150);
//    }
//
//    @Override
//    public int getZIndex() {return 50;}
//
//    @Override
//    public CellLayout.Zone getZone() {
//        return CellLayout.Zone.PRIMARY;
//    }
//
//    @Override
//    public ImageResource getImageType() {
//        return null;
//    }
//
//    @Override
//    public BufferedImage getImage(ResourceProvider<BufferedImage, ImageResource> provider) {
//        return null;
//    }
//}