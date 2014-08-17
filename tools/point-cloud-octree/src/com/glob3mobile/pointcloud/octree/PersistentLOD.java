

package com.glob3mobile.pointcloud.octree;

import java.util.Collections;
import java.util.List;

import es.igosoftware.util.GStringUtils;


public interface PersistentLOD
         extends
            AutoCloseable {


   public class Level {
      private final int              _level;
      private final List<Geodetic3D> _points;


      public Level(final int level,
                   final List<Geodetic3D> points) {
         _level = level;
         _points = Collections.unmodifiableList(points);
      }


      @Override
      public String toString() {
         final double estimatedSize = _points.size() * 3 * 8;
         return "[Level " + _level + //
                  ", points=" + _points.size() + //
                  ", estimatedSize=" + GStringUtils.getSpaceMessage(estimatedSize) + "]";
      }


      public int size() {
         return _points.size();
      }


      public int getLevel() {
         return _level;
      }


      public List<Geodetic3D> getPoints() {
         return _points;
      }

   }


   public interface Node {
      @Override
      String toString();


      String getID();


      //      boolean isDirty();


      int getPointsCount();


      List<Geodetic3D> getPoints();


      List<Geodetic3D> getPoints(PersistentLOD.Transaction transaction);


      Sector getSector();


      int getLevel();


   }

   public interface Visitor {

      void start(PersistentLOD.Transaction transaction);


      void stop(PersistentLOD.Transaction transaction);


      boolean visit(PersistentLOD.Transaction transaction,
                    PersistentLOD.Node node);

   }


   public static interface Transaction {

      void commit();


      void rollback();

   }


   PersistentLOD.Transaction createTransaction();


   void put(PersistentLOD.Transaction transaction,
            String id,
            int level,
            //boolean dirty,
            List<Geodetic3D> points);


   //   void putOrMerge(PersistentLOD.Transaction transaction,
   //                   String id,
   //                   boolean dirty,
   //                   List<Geodetic3D> points);


   @Override
   void close();


   String getCloudName();


   void acceptDepthFirstVisitor(PersistentLOD.Transaction transaction,
                                PersistentLOD.Visitor visitor);


   List<PersistentLOD.Level> getLODLevels(String id);


   Sector getSector(String id);

}