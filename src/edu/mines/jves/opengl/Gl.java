/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jves.opengl;

import java.nio.*;

/**
 * OpenGL standard constants and methods. Where possible, standard OpenGL
 * function names and argument types are preserved. For convenience, some
 * methods are overloaded; e.g., glVertex2f(float,float) can be written
 * simply as glVertex(float,float).
 * <p>
 * All methods in this class are static. These methods should be called
 * only while an OpenGL context ({@link edu.mines.jves.opengl.GlContext})
 * is locked for the calling thread.
 * <p>
 * Some methods, such as 
 * {@link #glVertexPointer(int,int,int,java.nio.Buffer)}, have an NIO
 * buffer argument. For such methods, these buffers must be <em>direct</em> 
 * NIO buffers. Unlike the contents of Java arrays, the contents of direct 
 * NIO buffers never move during garbage collection. For example, the
 * vertices specified by calling 
 * {@link #glVertexPointer(int,int,int,java.nio.Buffer)}
 * will not move before a corresponding call to, say,
 * {@link #glDrawArrays(int,int,int)}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2004.11.24
 */
public class Gl {

  ///////////////////////////////////////////////////////////////////////////
  // constants

  public static final int GL_CURRENT_BIT                         =0x00000001;
  public static final int GL_POINT_BIT                           =0x00000002;
  public static final int GL_LINE_BIT                            =0x00000004;
  public static final int GL_POLYGON_BIT                         =0x00000008;
  public static final int GL_POLYGON_STIPPLE_BIT                 =0x00000010;
  public static final int GL_PIXEL_MODE_BIT                      =0x00000020;
  public static final int GL_LIGHTING_BIT                        =0x00000040;
  public static final int GL_FOG_BIT                             =0x00000080;
  public static final int GL_DEPTH_BUFFER_BIT                    =0x00000100;
  public static final int GL_ACCUM_BUFFER_BIT                    =0x00000200;
  public static final int GL_STENCIL_BUFFER_BIT                  =0x00000400;
  public static final int GL_VIEWPORT_BIT                        =0x00000800;
  public static final int GL_TRANSFORM_BIT                       =0x00001000;
  public static final int GL_ENABLE_BIT                          =0x00002000;
  public static final int GL_COLOR_BUFFER_BIT                    =0x00004000;
  public static final int GL_HINT_BIT                            =0x00008000;
  public static final int GL_EVAL_BIT                            =0x00010000;
  public static final int GL_LIST_BIT                            =0x00020000;
  public static final int GL_TEXTURE_BIT                         =0x00040000;
  public static final int GL_SCISSOR_BIT                         =0x00080000;
  public static final int GL_ALL_ATTRIB_BITS                     =0xFFFFFFFF;

  public static final int GL_CLIENT_PIXEL_STORE_BIT              =0x00000001;
  public static final int GL_CLIENT_VERTEX_ARRAY_BIT             =0x00000002;
  public static final int GL_CLIENT_ALL_ATTRIB_BITS              =0xFFFFFFFF;

  public static final int GL_FALSE                                        =0;
  public static final int GL_TRUE                                         =1;

  public static final int GL_POINTS                                  =0x0000;
  public static final int GL_LINES                                   =0x0001;
  public static final int GL_LINE_LOOP                               =0x0002;
  public static final int GL_LINE_STRIP                              =0x0003;
  public static final int GL_TRIANGLES                               =0x0004;
  public static final int GL_TRIANGLE_STRIP                          =0x0005;
  public static final int GL_TRIANGLE_FAN                            =0x0006;
  public static final int GL_QUADS                                   =0x0007;
  public static final int GL_QUAD_STRIP                              =0x0008;
  public static final int GL_POLYGON                                 =0x0009;

  public static final int GL_ACCUM                                   =0x0100;
  public static final int GL_LOAD                                    =0x0101;
  public static final int GL_RETURN                                  =0x0102;
  public static final int GL_MULT                                    =0x0103;
  public static final int GL_ADD                                     =0x0104;

  public static final int GL_NEVER                                   =0x0200;
  public static final int GL_LESS                                    =0x0201;
  public static final int GL_EQUAL                                   =0x0202;
  public static final int GL_LEQUAL                                  =0x0203;
  public static final int GL_GREATER                                 =0x0204;
  public static final int GL_NOTEQUAL                                =0x0205;
  public static final int GL_GEQUAL                                  =0x0206;
  public static final int GL_ALWAYS                                  =0x0207;

  public static final int GL_ZERO                                         =0;
  public static final int GL_ONE                                          =1;
  public static final int GL_SRC_COLOR                               =0x0300;
  public static final int GL_ONE_MINUS_SRC_COLOR                     =0x0301;
  public static final int GL_SRC_ALPHA                               =0x0302;
  public static final int GL_ONE_MINUS_SRC_ALPHA                     =0x0303;
  public static final int GL_DST_ALPHA                               =0x0304;
  public static final int GL_ONE_MINUS_DST_ALPHA                     =0x0305;

  public static final int GL_DST_COLOR                               =0x0306;
  public static final int GL_ONE_MINUS_DST_COLOR                     =0x0307;
  public static final int GL_SRC_ALPHA_SATURATE                      =0x0308;

  public static final int GL_NONE                                         =0;
  public static final int GL_FRONT_LEFT                              =0x0400;
  public static final int GL_FRONT_RIGHT                             =0x0401;
  public static final int GL_BACK_LEFT                               =0x0402;
  public static final int GL_BACK_RIGHT                              =0x0403;
  public static final int GL_FRONT                                   =0x0404;
  public static final int GL_BACK                                    =0x0405;
  public static final int GL_LEFT                                    =0x0406;
  public static final int GL_RIGHT                                   =0x0407;
  public static final int GL_FRONT_AND_BACK                          =0x0408;
  public static final int GL_AUX0                                    =0x0409;
  public static final int GL_AUX1                                    =0x040A;
  public static final int GL_AUX2                                    =0x040B;
  public static final int GL_AUX3                                    =0x040C;

  public static final int GL_NO_ERROR                                     =0;
  public static final int GL_INVALID_ENUM                            =0x0500;
  public static final int GL_INVALID_VALUE                           =0x0501;
  public static final int GL_INVALID_OPERATION                       =0x0502;
  public static final int GL_STACK_OVERFLOW                          =0x0503;
  public static final int GL_STACK_UNDERFLOW                         =0x0504;
  public static final int GL_OUT_OF_MEMORY                           =0x0505;

  public static final int GL_2D                                      =0x0600;
  public static final int GL_3D                                      =0x0601;
  public static final int GL_3D_COLOR                                =0x0602;
  public static final int GL_3D_COLOR_TEXTURE                        =0x0603;
  public static final int GL_4D_COLOR_TEXTURE                        =0x0604;

  public static final int GL_PASS_THROUGH_TOKEN                      =0x0700;
  public static final int GL_POINT_TOKEN                             =0x0701;
  public static final int GL_LINE_TOKEN                              =0x0702;
  public static final int GL_POLYGON_TOKEN                           =0x0703;
  public static final int GL_BITMAP_TOKEN                            =0x0704;
  public static final int GL_DRAW_PIXEL_TOKEN                        =0x0705;
  public static final int GL_COPY_PIXEL_TOKEN                        =0x0706;
  public static final int GL_LINE_RESET_TOKEN                        =0x0707;

  public static final int GL_EXP                                     =0x0800;
  public static final int GL_EXP2                                    =0x0801;

  public static final int GL_CW                                      =0x0900;
  public static final int GL_CCW                                     =0x0901;

  public static final int GL_COEFF                                   =0x0A00;
  public static final int GL_ORDER                                   =0x0A01;
  public static final int GL_DOMAIN                                  =0x0A02;

  public static final int GL_PIXEL_MAP_I_TO_I                        =0x0C70;
  public static final int GL_PIXEL_MAP_S_TO_S                        =0x0C71;
  public static final int GL_PIXEL_MAP_I_TO_R                        =0x0C72;
  public static final int GL_PIXEL_MAP_I_TO_G                        =0x0C73;
  public static final int GL_PIXEL_MAP_I_TO_B                        =0x0C74;
  public static final int GL_PIXEL_MAP_I_TO_A                        =0x0C75;
  public static final int GL_PIXEL_MAP_R_TO_R                        =0x0C76;
  public static final int GL_PIXEL_MAP_G_TO_G                        =0x0C77;
  public static final int GL_PIXEL_MAP_B_TO_B                        =0x0C78;
  public static final int GL_PIXEL_MAP_A_TO_A                        =0x0C79;

  public static final int GL_VERTEX_ARRAY_POINTER                    =0x808E;
  public static final int GL_NORMAL_ARRAY_POINTER                    =0x808F;
  public static final int GL_COLOR_ARRAY_POINTER                     =0x8090;
  public static final int GL_INDEX_ARRAY_POINTER                     =0x8091;
  public static final int GL_TEXTURE_COORD_ARRAY_POINTER             =0x8092;
  public static final int GL_EDGE_FLAG_ARRAY_POINTER                 =0x8093;

  public static final int GL_CURRENT_COLOR                           =0x0B00;
  public static final int GL_CURRENT_INDEX                           =0x0B01;
  public static final int GL_CURRENT_NORMAL                          =0x0B02;
  public static final int GL_CURRENT_TEXTURE_COORDS                  =0x0B03;
  public static final int GL_CURRENT_RASTER_COLOR                    =0x0B04;
  public static final int GL_CURRENT_RASTER_INDEX                    =0x0B05;
  public static final int GL_CURRENT_RASTER_TEXTURE_COORDS           =0x0B06;
  public static final int GL_CURRENT_RASTER_POSITION                 =0x0B07;
  public static final int GL_CURRENT_RASTER_POSITION_VALID           =0x0B08;
  public static final int GL_CURRENT_RASTER_DISTANCE                 =0x0B09;
  public static final int GL_POINT_SMOOTH                            =0x0B10;
  public static final int GL_POINT_SIZE                              =0x0B11;
  public static final int GL_POINT_SIZE_RANGE                        =0x0B12;
  public static final int GL_POINT_SIZE_GRANULARITY                  =0x0B13;
  public static final int GL_LINE_SMOOTH                             =0x0B20;
  public static final int GL_LINE_WIDTH                              =0x0B21;
  public static final int GL_LINE_WIDTH_RANGE                        =0x0B22;
  public static final int GL_LINE_WIDTH_GRANULARITY                  =0x0B23;
  public static final int GL_LINE_STIPPLE                            =0x0B24;
  public static final int GL_LINE_STIPPLE_PATTERN                    =0x0B25;
  public static final int GL_LINE_STIPPLE_REPEAT                     =0x0B26;
  public static final int GL_LIST_MODE                               =0x0B30;
  public static final int GL_MAX_LIST_NESTING                        =0x0B31;
  public static final int GL_LIST_BASE                               =0x0B32;
  public static final int GL_LIST_INDEX                              =0x0B33;
  public static final int GL_POLYGON_MODE                            =0x0B40;
  public static final int GL_POLYGON_SMOOTH                          =0x0B41;
  public static final int GL_POLYGON_STIPPLE                         =0x0B42;
  public static final int GL_EDGE_FLAG                               =0x0B43;
  public static final int GL_CULL_FACE                               =0x0B44;
  public static final int GL_CULL_FACE_MODE                          =0x0B45;
  public static final int GL_FRONT_FACE                              =0x0B46;
  public static final int GL_LIGHTING                                =0x0B50;
  public static final int GL_LIGHT_MODEL_LOCAL_VIEWER                =0x0B51;
  public static final int GL_LIGHT_MODEL_TWO_SIDE                    =0x0B52;
  public static final int GL_LIGHT_MODEL_AMBIENT                     =0x0B53;
  public static final int GL_SHADE_MODEL                             =0x0B54;
  public static final int GL_COLOR_MATERIAL_FACE                     =0x0B55;
  public static final int GL_COLOR_MATERIAL_PARAMETER                =0x0B56;
  public static final int GL_COLOR_MATERIAL                          =0x0B57;
  public static final int GL_FOG                                     =0x0B60;
  public static final int GL_FOG_INDEX                               =0x0B61;
  public static final int GL_FOG_DENSITY                             =0x0B62;
  public static final int GL_FOG_START                               =0x0B63;
  public static final int GL_FOG_END                                 =0x0B64;
  public static final int GL_FOG_MODE                                =0x0B65;
  public static final int GL_FOG_COLOR                               =0x0B66;
  public static final int GL_DEPTH_RANGE                             =0x0B70;
  public static final int GL_DEPTH_TEST                              =0x0B71;
  public static final int GL_DEPTH_WRITEMASK                         =0x0B72;
  public static final int GL_DEPTH_CLEAR_VALUE                       =0x0B73;
  public static final int GL_DEPTH_FUNC                              =0x0B74;
  public static final int GL_ACCUM_CLEAR_VALUE                       =0x0B80;
  public static final int GL_STENCIL_TEST                            =0x0B90;
  public static final int GL_STENCIL_CLEAR_VALUE                     =0x0B91;
  public static final int GL_STENCIL_FUNC                            =0x0B92;
  public static final int GL_STENCIL_VALUE_MASK                      =0x0B93;
  public static final int GL_STENCIL_FAIL                            =0x0B94;
  public static final int GL_STENCIL_PASS_DEPTH_FAIL                 =0x0B95;
  public static final int GL_STENCIL_PASS_DEPTH_PASS                 =0x0B96;
  public static final int GL_STENCIL_REF                             =0x0B97;
  public static final int GL_STENCIL_WRITEMASK                       =0x0B98;
  public static final int GL_MATRIX_MODE                             =0x0BA0;
  public static final int GL_NORMALIZE                               =0x0BA1;
  public static final int GL_VIEWPORT                                =0x0BA2;
  public static final int GL_MODELVIEW_STACK_DEPTH                   =0x0BA3;
  public static final int GL_PROJECTION_STACK_DEPTH                  =0x0BA4;
  public static final int GL_TEXTURE_STACK_DEPTH                     =0x0BA5;
  public static final int GL_MODELVIEW_MATRIX                        =0x0BA6;
  public static final int GL_PROJECTION_MATRIX                       =0x0BA7;
  public static final int GL_TEXTURE_MATRIX                          =0x0BA8;
  public static final int GL_ATTRIB_STACK_DEPTH                      =0x0BB0;
  public static final int GL_CLIENT_ATTRIB_STACK_DEPTH               =0x0BB1;
  public static final int GL_ALPHA_TEST                              =0x0BC0;
  public static final int GL_ALPHA_TEST_FUNC                         =0x0BC1;
  public static final int GL_ALPHA_TEST_REF                          =0x0BC2;
  public static final int GL_DITHER                                  =0x0BD0;
  public static final int GL_BLEND_DST                               =0x0BE0;
  public static final int GL_BLEND_SRC                               =0x0BE1;
  public static final int GL_BLEND                                   =0x0BE2;
  public static final int GL_LOGIC_OP_MODE                           =0x0BF0;
  public static final int GL_INDEX_LOGIC_OP                          =0x0BF1;
  public static final int GL_LOGIC_OP                                =0x0BF1;
  public static final int GL_COLOR_LOGIC_OP                          =0x0BF2;
  public static final int GL_AUX_BUFFERS                             =0x0C00;
  public static final int GL_DRAW_BUFFER                             =0x0C01;
  public static final int GL_READ_BUFFER                             =0x0C02;
  public static final int GL_SCISSOR_BOX                             =0x0C10;
  public static final int GL_SCISSOR_TEST                            =0x0C11;
  public static final int GL_INDEX_CLEAR_VALUE                       =0x0C20;
  public static final int GL_INDEX_WRITEMASK                         =0x0C21;
  public static final int GL_COLOR_CLEAR_VALUE                       =0x0C22;
  public static final int GL_COLOR_WRITEMASK                         =0x0C23;
  public static final int GL_INDEX_MODE                              =0x0C30;
  public static final int GL_RGBA_MODE                               =0x0C31;
  public static final int GL_DOUBLEBUFFER                            =0x0C32;
  public static final int GL_STEREO                                  =0x0C33;
  public static final int GL_RENDER_MODE                             =0x0C40;
  public static final int GL_PERSPECTIVE_CORRECTION_HINT             =0x0C50;
  public static final int GL_POINT_SMOOTH_HINT                       =0x0C51;
  public static final int GL_LINE_SMOOTH_HINT                        =0x0C52;
  public static final int GL_POLYGON_SMOOTH_HINT                     =0x0C53;
  public static final int GL_FOG_HINT                                =0x0C54;
  public static final int GL_TEXTURE_GEN_S                           =0x0C60;
  public static final int GL_TEXTURE_GEN_T                           =0x0C61;
  public static final int GL_TEXTURE_GEN_R                           =0x0C62;
  public static final int GL_TEXTURE_GEN_Q                           =0x0C63;
  public static final int GL_PIXEL_MAP_I_TO_I_SIZE                   =0x0CB0;
  public static final int GL_PIXEL_MAP_S_TO_S_SIZE                   =0x0CB1;
  public static final int GL_PIXEL_MAP_I_TO_R_SIZE                   =0x0CB2;
  public static final int GL_PIXEL_MAP_I_TO_G_SIZE                   =0x0CB3;
  public static final int GL_PIXEL_MAP_I_TO_B_SIZE                   =0x0CB4;
  public static final int GL_PIXEL_MAP_I_TO_A_SIZE                   =0x0CB5;
  public static final int GL_PIXEL_MAP_R_TO_R_SIZE                   =0x0CB6;
  public static final int GL_PIXEL_MAP_G_TO_G_SIZE                   =0x0CB7;
  public static final int GL_PIXEL_MAP_B_TO_B_SIZE                   =0x0CB8;
  public static final int GL_PIXEL_MAP_A_TO_A_SIZE                   =0x0CB9;
  public static final int GL_UNPACK_SWAP_BYTES                       =0x0CF0;
  public static final int GL_UNPACK_LSB_FIRST                        =0x0CF1;
  public static final int GL_UNPACK_ROW_LENGTH                       =0x0CF2;
  public static final int GL_UNPACK_SKIP_ROWS                        =0x0CF3;
  public static final int GL_UNPACK_SKIP_PIXELS                      =0x0CF4;
  public static final int GL_UNPACK_ALIGNMENT                        =0x0CF5;
  public static final int GL_PACK_SWAP_BYTES                         =0x0D00;
  public static final int GL_PACK_LSB_FIRST                          =0x0D01;
  public static final int GL_PACK_ROW_LENGTH                         =0x0D02;
  public static final int GL_PACK_SKIP_ROWS                          =0x0D03;
  public static final int GL_PACK_SKIP_PIXELS                        =0x0D04;
  public static final int GL_PACK_ALIGNMENT                          =0x0D05;
  public static final int GL_MAP_COLOR                               =0x0D10;
  public static final int GL_MAP_STENCIL                             =0x0D11;
  public static final int GL_INDEX_SHIFT                             =0x0D12;
  public static final int GL_INDEX_OFFSET                            =0x0D13;
  public static final int GL_RED_SCALE                               =0x0D14;
  public static final int GL_RED_BIAS                                =0x0D15;
  public static final int GL_ZOOM_X                                  =0x0D16;
  public static final int GL_ZOOM_Y                                  =0x0D17;
  public static final int GL_GREEN_SCALE                             =0x0D18;
  public static final int GL_GREEN_BIAS                              =0x0D19;
  public static final int GL_BLUE_SCALE                              =0x0D1A;
  public static final int GL_BLUE_BIAS                               =0x0D1B;
  public static final int GL_ALPHA_SCALE                             =0x0D1C;
  public static final int GL_ALPHA_BIAS                              =0x0D1D;
  public static final int GL_DEPTH_SCALE                             =0x0D1E;
  public static final int GL_DEPTH_BIAS                              =0x0D1F;
  public static final int GL_MAX_EVAL_ORDER                          =0x0D30;
  public static final int GL_MAX_LIGHTS                              =0x0D31;
  public static final int GL_MAX_CLIP_PLANES                         =0x0D32;
  public static final int GL_MAX_TEXTURE_SIZE                        =0x0D33;
  public static final int GL_MAX_PIXEL_MAP_TABLE                     =0x0D34;
  public static final int GL_MAX_ATTRIB_STACK_DEPTH                  =0x0D35;
  public static final int GL_MAX_MODELVIEW_STACK_DEPTH               =0x0D36;
  public static final int GL_MAX_NAME_STACK_DEPTH                    =0x0D37;
  public static final int GL_MAX_PROJECTION_STACK_DEPTH              =0x0D38;
  public static final int GL_MAX_TEXTURE_STACK_DEPTH                 =0x0D39;
  public static final int GL_MAX_VIEWPORT_DIMS                       =0x0D3A;
  public static final int GL_MAX_CLIENT_ATTRIB_STACK_DEPTH           =0x0D3B;
  public static final int GL_SUBPIXEL_BITS                           =0x0D50;
  public static final int GL_INDEX_BITS                              =0x0D51;
  public static final int GL_RED_BITS                                =0x0D52;
  public static final int GL_GREEN_BITS                              =0x0D53;
  public static final int GL_BLUE_BITS                               =0x0D54;
  public static final int GL_ALPHA_BITS                              =0x0D55;
  public static final int GL_DEPTH_BITS                              =0x0D56;
  public static final int GL_STENCIL_BITS                            =0x0D57;
  public static final int GL_ACCUM_RED_BITS                          =0x0D58;
  public static final int GL_ACCUM_GREEN_BITS                        =0x0D59;
  public static final int GL_ACCUM_BLUE_BITS                         =0x0D5A;
  public static final int GL_ACCUM_ALPHA_BITS                        =0x0D5B;
  public static final int GL_NAME_STACK_DEPTH                        =0x0D70;
  public static final int GL_AUTO_NORMAL                             =0x0D80;
  public static final int GL_MAP1_COLOR_4                            =0x0D90;
  public static final int GL_MAP1_INDEX                              =0x0D91;
  public static final int GL_MAP1_NORMAL                             =0x0D92;
  public static final int GL_MAP1_TEXTURE_COORD_1                    =0x0D93;
  public static final int GL_MAP1_TEXTURE_COORD_2                    =0x0D94;
  public static final int GL_MAP1_TEXTURE_COORD_3                    =0x0D95;
  public static final int GL_MAP1_TEXTURE_COORD_4                    =0x0D96;
  public static final int GL_MAP1_VERTEX_3                           =0x0D97;
  public static final int GL_MAP1_VERTEX_4                           =0x0D98;
  public static final int GL_MAP2_COLOR_4                            =0x0DB0;
  public static final int GL_MAP2_INDEX                              =0x0DB1;
  public static final int GL_MAP2_NORMAL                             =0x0DB2;
  public static final int GL_MAP2_TEXTURE_COORD_1                    =0x0DB3;
  public static final int GL_MAP2_TEXTURE_COORD_2                    =0x0DB4;
  public static final int GL_MAP2_TEXTURE_COORD_3                    =0x0DB5;
  public static final int GL_MAP2_TEXTURE_COORD_4                    =0x0DB6;
  public static final int GL_MAP2_VERTEX_3                           =0x0DB7;
  public static final int GL_MAP2_VERTEX_4                           =0x0DB8;
  public static final int GL_MAP1_GRID_DOMAIN                        =0x0DD0;
  public static final int GL_MAP1_GRID_SEGMENTS                      =0x0DD1;
  public static final int GL_MAP2_GRID_DOMAIN                        =0x0DD2;
  public static final int GL_MAP2_GRID_SEGMENTS                      =0x0DD3;
  public static final int GL_TEXTURE_1D                              =0x0DE0;
  public static final int GL_TEXTURE_2D                              =0x0DE1;
  public static final int GL_FEEDBACK_BUFFER_POINTER                 =0x0DF0;
  public static final int GL_FEEDBACK_BUFFER_SIZE                    =0x0DF1;
  public static final int GL_FEEDBACK_BUFFER_TYPE                    =0x0DF2;
  public static final int GL_SELECTION_BUFFER_POINTER                =0x0DF3;
  public static final int GL_SELECTION_BUFFER_SIZE                   =0x0DF4;
  public static final int GL_POLYGON_OFFSET_UNITS                    =0x2A00;
  public static final int GL_POLYGON_OFFSET_POINT                    =0x2A01;
  public static final int GL_POLYGON_OFFSET_LINE                     =0x2A02;
  public static final int GL_POLYGON_OFFSET_FILL                     =0x8037;
  public static final int GL_POLYGON_OFFSET_FACTOR                   =0x8038;
  public static final int GL_TEXTURE_BINDING_1D                      =0x8068;
  public static final int GL_TEXTURE_BINDING_2D                      =0x8069;
  public static final int GL_VERTEX_ARRAY                            =0x8074;
  public static final int GL_NORMAL_ARRAY                            =0x8075;
  public static final int GL_COLOR_ARRAY                             =0x8076;
  public static final int GL_INDEX_ARRAY                             =0x8077;
  public static final int GL_TEXTURE_COORD_ARRAY                     =0x8078;
  public static final int GL_EDGE_FLAG_ARRAY                         =0x8079;
  public static final int GL_VERTEX_ARRAY_SIZE                       =0x807A;
  public static final int GL_VERTEX_ARRAY_TYPE                       =0x807B;
  public static final int GL_VERTEX_ARRAY_STRIDE                     =0x807C;
  public static final int GL_NORMAL_ARRAY_TYPE                       =0x807E;
  public static final int GL_NORMAL_ARRAY_STRIDE                     =0x807F;
  public static final int GL_COLOR_ARRAY_SIZE                        =0x8081;
  public static final int GL_COLOR_ARRAY_TYPE                        =0x8082;
  public static final int GL_COLOR_ARRAY_STRIDE                      =0x8083;
  public static final int GL_INDEX_ARRAY_TYPE                        =0x8085;
  public static final int GL_INDEX_ARRAY_STRIDE                      =0x8086;
  public static final int GL_TEXTURE_COORD_ARRAY_SIZE                =0x8088;
  public static final int GL_TEXTURE_COORD_ARRAY_TYPE                =0x8089;
  public static final int GL_TEXTURE_COORD_ARRAY_STRIDE              =0x808A;
  public static final int GL_EDGE_FLAG_ARRAY_STRIDE                  =0x808C;

  public static final int GL_TEXTURE_WIDTH                           =0x1000;
  public static final int GL_TEXTURE_HEIGHT                          =0x1001;
  public static final int GL_TEXTURE_INTERNAL_FORMAT                 =0x1003;
  public static final int GL_TEXTURE_COMPONENTS                      =0x1003;
  public static final int GL_TEXTURE_BORDER_COLOR                    =0x1004;
  public static final int GL_TEXTURE_BORDER                          =0x1005;
  public static final int GL_TEXTURE_RED_SIZE                        =0x805C;
  public static final int GL_TEXTURE_GREEN_SIZE                      =0x805D;
  public static final int GL_TEXTURE_BLUE_SIZE                       =0x805E;
  public static final int GL_TEXTURE_ALPHA_SIZE                      =0x805F;
  public static final int GL_TEXTURE_LUMINANCE_SIZE                  =0x8060;
  public static final int GL_TEXTURE_INTENSITY_SIZE                  =0x8061;
  public static final int GL_TEXTURE_PRIORITY                        =0x8066;
  public static final int GL_TEXTURE_RESIDENT                        =0x8067;

  public static final int GL_DONT_CARE                               =0x1100;
  public static final int GL_FASTEST                                 =0x1101;
  public static final int GL_NICEST                                  =0x1102;

  public static final int GL_AMBIENT                                 =0x1200;
  public static final int GL_DIFFUSE                                 =0x1201;
  public static final int GL_SPECULAR                                =0x1202;
  public static final int GL_POSITION                                =0x1203;
  public static final int GL_SPOT_DIRECTION                          =0x1204;
  public static final int GL_SPOT_EXPONENT                           =0x1205;
  public static final int GL_SPOT_CUTOFF                             =0x1206;
  public static final int GL_CONSTANT_ATTENUATION                    =0x1207;
  public static final int GL_LINEAR_ATTENUATION                      =0x1208;
  public static final int GL_QUADRATIC_ATTENUATION                   =0x1209;

  public static final int GL_COMPILE                                 =0x1300;
  public static final int GL_COMPILE_AND_EXECUTE                     =0x1301;

  public static final int GL_BYTE                                    =0x1400;
  public static final int GL_UNSIGNED_BYTE                           =0x1401;
  public static final int GL_SHORT                                   =0x1402;
  public static final int GL_UNSIGNED_SHORT                          =0x1403;
  public static final int GL_INT                                     =0x1404;
  public static final int GL_UNSIGNED_INT                            =0x1405;
  public static final int GL_FLOAT                                   =0x1406;
  public static final int GL_2_BYTES                                 =0x1407;
  public static final int GL_3_BYTES                                 =0x1408;
  public static final int GL_4_BYTES                                 =0x1409;
  public static final int GL_DOUBLE                                  =0x140A;
  public static final int GL_DOUBLE_EXT                              =0x140A;

  public static final int GL_CLEAR                                   =0x1500;
  public static final int GL_AND                                     =0x1501;
  public static final int GL_AND_REVERSE                             =0x1502;
  public static final int GL_COPY                                    =0x1503;
  public static final int GL_AND_INVERTED                            =0x1504;
  public static final int GL_NOOP                                    =0x1505;
  public static final int GL_XOR                                     =0x1506;
  public static final int GL_OR                                      =0x1507;
  public static final int GL_NOR                                     =0x1508;
  public static final int GL_EQUIV                                   =0x1509;
  public static final int GL_INVERT                                  =0x150A;
  public static final int GL_OR_REVERSE                              =0x150B;
  public static final int GL_COPY_INVERTED                           =0x150C;
  public static final int GL_OR_INVERTED                             =0x150D;
  public static final int GL_NAND                                    =0x150E;
  public static final int GL_SET                                     =0x150F;

  public static final int GL_EMISSION                                =0x1600;
  public static final int GL_SHININESS                               =0x1601;
  public static final int GL_AMBIENT_AND_DIFFUSE                     =0x1602;
  public static final int GL_COLOR_INDEXES                           =0x1603;

  public static final int GL_MODELVIEW                               =0x1700;
  public static final int GL_PROJECTION                              =0x1701;
  public static final int GL_TEXTURE                                 =0x1702;

  public static final int GL_COLOR                                   =0x1800;
  public static final int GL_DEPTH                                   =0x1801;
  public static final int GL_STENCIL                                 =0x1802;

  public static final int GL_COLOR_INDEX                             =0x1900;
  public static final int GL_STENCIL_INDEX                           =0x1901;
  public static final int GL_DEPTH_COMPONENT                         =0x1902;
  public static final int GL_RED                                     =0x1903;
  public static final int GL_GREEN                                   =0x1904;
  public static final int GL_BLUE                                    =0x1905;
  public static final int GL_ALPHA                                   =0x1906;
  public static final int GL_RGB                                     =0x1907;
  public static final int GL_RGBA                                    =0x1908;
  public static final int GL_LUMINANCE                               =0x1909;
  public static final int GL_LUMINANCE_ALPHA                         =0x190A;

  public static final int GL_BITMAP                                  =0x1A00;

  public static final int GL_POINT                                   =0x1B00;
  public static final int GL_LINE                                    =0x1B01;
  public static final int GL_FILL                                    =0x1B02;

  public static final int GL_RENDER                                  =0x1C00;
  public static final int GL_FEEDBACK                                =0x1C01;
  public static final int GL_SELECT                                  =0x1C02;

  public static final int GL_FLAT                                    =0x1D00;
  public static final int GL_SMOOTH                                  =0x1D01;

  public static final int GL_KEEP                                    =0x1E00;
  public static final int GL_REPLACE                                 =0x1E01;
  public static final int GL_INCR                                    =0x1E02;
  public static final int GL_DECR                                    =0x1E03;

  public static final int GL_VENDOR                                  =0x1F00;
  public static final int GL_RENDERER                                =0x1F01;
  public static final int GL_VERSION                                 =0x1F02;
  public static final int GL_EXTENSIONS                              =0x1F03;

  public static final int GL_S                                       =0x2000;
  public static final int GL_T                                       =0x2001;
  public static final int GL_R                                       =0x2002;
  public static final int GL_Q                                       =0x2003;

  public static final int GL_MODULATE                                =0x2100;
  public static final int GL_DECAL                                   =0x2101;

  public static final int GL_TEXTURE_ENV_MODE                        =0x2200;
  public static final int GL_TEXTURE_ENV_COLOR                       =0x2201;

  public static final int GL_TEXTURE_ENV                             =0x2300;

  public static final int GL_EYE_LINEAR                              =0x2400;
  public static final int GL_OBJECT_LINEAR                           =0x2401;
  public static final int GL_SPHERE_MAP                              =0x2402;

  public static final int GL_TEXTURE_GEN_MODE                        =0x2500;
  public static final int GL_OBJECT_PLANE                            =0x2501;
  public static final int GL_EYE_PLANE                               =0x2502;

  public static final int GL_NEAREST                                 =0x2600;
  public static final int GL_LINEAR                                  =0x2601;

  public static final int GL_NEAREST_MIPMAP_NEAREST                  =0x2700;
  public static final int GL_LINEAR_MIPMAP_NEAREST                   =0x2701;
  public static final int GL_NEAREST_MIPMAP_LINEAR                   =0x2702;
  public static final int GL_LINEAR_MIPMAP_LINEAR                    =0x2703;

  public static final int GL_TEXTURE_MAG_FILTER                      =0x2800;
  public static final int GL_TEXTURE_MIN_FILTER                      =0x2801;
  public static final int GL_TEXTURE_WRAP_S                          =0x2802;
  public static final int GL_TEXTURE_WRAP_T                          =0x2803;

  public static final int GL_PROXY_TEXTURE_1D                        =0x8063;
  public static final int GL_PROXY_TEXTURE_2D                        =0x8064;

  public static final int GL_CLAMP                                   =0x2900;
  public static final int GL_REPEAT                                  =0x2901;

  public static final int GL_R3_G3_B2                                =0x2A10;
  public static final int GL_ALPHA4                                  =0x803B;
  public static final int GL_ALPHA8                                  =0x803C;
  public static final int GL_ALPHA12                                 =0x803D;
  public static final int GL_ALPHA16                                 =0x803E;
  public static final int GL_LUMINANCE4                              =0x803F;
  public static final int GL_LUMINANCE8                              =0x8040;
  public static final int GL_LUMINANCE12                             =0x8041;
  public static final int GL_LUMINANCE16                             =0x8042;
  public static final int GL_LUMINANCE4_ALPHA4                       =0x8043;
  public static final int GL_LUMINANCE6_ALPHA2                       =0x8044;
  public static final int GL_LUMINANCE8_ALPHA8                       =0x8045;
  public static final int GL_LUMINANCE12_ALPHA4                      =0x8046;
  public static final int GL_LUMINANCE12_ALPHA12                     =0x8047;
  public static final int GL_LUMINANCE16_ALPHA16                     =0x8048;
  public static final int GL_INTENSITY                               =0x8049;
  public static final int GL_INTENSITY4                              =0x804A;
  public static final int GL_INTENSITY8                              =0x804B;
  public static final int GL_INTENSITY12                             =0x804C;
  public static final int GL_INTENSITY16                             =0x804D;
  public static final int GL_RGB4                                    =0x804F;
  public static final int GL_RGB5                                    =0x8050;
  public static final int GL_RGB8                                    =0x8051;
  public static final int GL_RGB10                                   =0x8052;
  public static final int GL_RGB12                                   =0x8053;
  public static final int GL_RGB16                                   =0x8054;
  public static final int GL_RGBA2                                   =0x8055;
  public static final int GL_RGBA4                                   =0x8056;
  public static final int GL_RGB5_A1                                 =0x8057;
  public static final int GL_RGBA8                                   =0x8058;
  public static final int GL_RGB10_A2                                =0x8059;
  public static final int GL_RGBA12                                  =0x805A;
  public static final int GL_RGBA16                                  =0x805B;

  public static final int GL_V2F                                     =0x2A20;
  public static final int GL_V3F                                     =0x2A21;
  public static final int GL_C4UB_V2F                                =0x2A22;
  public static final int GL_C4UB_V3F                                =0x2A23;
  public static final int GL_C3F_V3F                                 =0x2A24;
  public static final int GL_N3F_V3F                                 =0x2A25;
  public static final int GL_C4F_N3F_V3F                             =0x2A26;
  public static final int GL_T2F_V3F                                 =0x2A27;
  public static final int GL_T4F_V4F                                 =0x2A28;
  public static final int GL_T2F_C4UB_V3F                            =0x2A29;
  public static final int GL_T2F_C3F_V3F                             =0x2A2A;
  public static final int GL_T2F_N3F_V3F                             =0x2A2B;
  public static final int GL_T2F_C4F_N3F_V3F                         =0x2A2C;
  public static final int GL_T4F_C4F_N3F_V4F                         =0x2A2D;

  public static final int GL_CLIP_PLANE0                             =0x3000;
  public static final int GL_CLIP_PLANE1                             =0x3001;
  public static final int GL_CLIP_PLANE2                             =0x3002;
  public static final int GL_CLIP_PLANE3                             =0x3003;
  public static final int GL_CLIP_PLANE4                             =0x3004;
  public static final int GL_CLIP_PLANE5                             =0x3005;

  public static final int GL_LIGHT0                                  =0x4000;
  public static final int GL_LIGHT1                                  =0x4001;
  public static final int GL_LIGHT2                                  =0x4002;
  public static final int GL_LIGHT3                                  =0x4003;
  public static final int GL_LIGHT4                                  =0x4004;
  public static final int GL_LIGHT5                                  =0x4005;
  public static final int GL_LIGHT6                                  =0x4006;
  public static final int GL_LIGHT7                                  =0x4007;

  // OpenGL 1.2
  public static final int GL_UNSIGNED_BYTE_3_3_2                     =0x8032;
  public static final int GL_UNSIGNED_SHORT_4_4_4_4                  =0x8033;
  public static final int GL_UNSIGNED_SHORT_5_5_5_1                  =0x8034;
  public static final int GL_UNSIGNED_INT_8_8_8_8                    =0x8035;
  public static final int GL_UNSIGNED_INT_10_10_10_2                 =0x8036;
  public static final int GL_RESCALE_NORMAL                          =0x803A;
  public static final int GL_TEXTURE_BINDING_3D                      =0x806A;
  public static final int GL_PACK_SKIP_IMAGES                        =0x806B;
  public static final int GL_PACK_IMAGE_HEIGHT                       =0x806C;
  public static final int GL_UNPACK_SKIP_IMAGES                      =0x806D;
  public static final int GL_UNPACK_IMAGE_HEIGHT                     =0x806E;
  public static final int GL_TEXTURE_3D                              =0x806F;
  public static final int GL_PROXY_TEXTURE_3D                        =0x8070;
  public static final int GL_TEXTURE_DEPTH                           =0x8071;
  public static final int GL_TEXTURE_WRAP_R                          =0x8072;
  public static final int GL_MAX_3D_TEXTURE_SIZE                     =0x8073;
  public static final int GL_UNSIGNED_BYTE_2_3_3_REV                 =0x8362;
  public static final int GL_UNSIGNED_SHORT_5_6_5                    =0x8363;
  public static final int GL_UNSIGNED_SHORT_5_6_5_REV                =0x8364;
  public static final int GL_UNSIGNED_SHORT_4_4_4_4_REV              =0x8365;
  public static final int GL_UNSIGNED_SHORT_1_5_5_5_REV              =0x8366;
  public static final int GL_UNSIGNED_INT_8_8_8_8_REV                =0x8367;
  public static final int GL_UNSIGNED_INT_2_10_10_10_REV             =0x8368;
  public static final int GL_BGR                                     =0x80E0;
  public static final int GL_BGRA                                    =0x80E1;
  public static final int GL_MAX_ELEMENTS_VERTICES                   =0x80E8;
  public static final int GL_MAX_ELEMENTS_INDICES                    =0x80E9;
  public static final int GL_CLAMP_TO_EDGE                           =0x812F;
  public static final int GL_TEXTURE_MIN_LOD                         =0x813A;
  public static final int GL_TEXTURE_MAX_LOD                         =0x813B;
  public static final int GL_TEXTURE_BASE_LEVEL                      =0x813C;
  public static final int GL_TEXTURE_MAX_LEVEL                       =0x813D;
  public static final int GL_LIGHT_MODEL_COLOR_CONTROL               =0x81F8;
  public static final int GL_SINGLE_COLOR                            =0x81F9;
  public static final int GL_SEPARATE_SPECULAR_COLOR                 =0x81FA;
  public static final int GL_SMOOTH_POINT_SIZE_RANGE                 =0x0B12;
  public static final int GL_SMOOTH_POINT_SIZE_GRANULARITY           =0x0B13;
  public static final int GL_SMOOTH_LINE_WIDTH_RANGE                 =0x0B22;
  public static final int GL_SMOOTH_LINE_WIDTH_GRANULARITY           =0x0B23;
  public static final int GL_ALIASED_POINT_SIZE_RANGE                =0x846D;
  public static final int GL_ALIASED_LINE_WIDTH_RANGE                =0x846E;

  public static final int GL_CONSTANT_COLOR                          =0x8001;
  public static final int GL_ONE_MINUS_CONSTANT_COLOR                =0x8002;
  public static final int GL_CONSTANT_ALPHA                          =0x8003;
  public static final int GL_ONE_MINUS_CONSTANT_ALPHA                =0x8004;
  public static final int GL_BLEND_COLOR                             =0x8005;
  public static final int GL_FUNC_ADD                                =0x8006;
  public static final int GL_MIN                                     =0x8007;
  public static final int GL_MAX                                     =0x8008;
  public static final int GL_BLEND_EQUATION                          =0x8009;
  public static final int GL_FUNC_SUBTRACT                           =0x800A;
  public static final int GL_FUNC_REVERSE_SUBTRACT                   =0x800B;
  public static final int GL_CONVOLUTION_1D                          =0x8010;
  public static final int GL_CONVOLUTION_2D                          =0x8011;
  public static final int GL_SEPARABLE_2D                            =0x8012;
  public static final int GL_CONVOLUTION_BORDER_MODE                 =0x8013;
  public static final int GL_CONVOLUTION_FILTER_SCALE                =0x8014;
  public static final int GL_CONVOLUTION_FILTER_BIAS                 =0x8015;
  public static final int GL_REDUCE                                  =0x8016;
  public static final int GL_CONVOLUTION_FORMAT                      =0x8017;
  public static final int GL_CONVOLUTION_WIDTH                       =0x8018;
  public static final int GL_CONVOLUTION_HEIGHT                      =0x8019;
  public static final int GL_MAX_CONVOLUTION_WIDTH                   =0x801A;
  public static final int GL_MAX_CONVOLUTION_HEIGHT                  =0x801B;
  public static final int GL_POST_CONVOLUTION_RED_SCALE              =0x801C;
  public static final int GL_POST_CONVOLUTION_GREEN_SCALE            =0x801D;
  public static final int GL_POST_CONVOLUTION_BLUE_SCALE             =0x801E;
  public static final int GL_POST_CONVOLUTION_ALPHA_SCALE            =0x801F;
  public static final int GL_POST_CONVOLUTION_RED_BIAS               =0x8020;
  public static final int GL_POST_CONVOLUTION_GREEN_BIAS             =0x8021;
  public static final int GL_POST_CONVOLUTION_BLUE_BIAS              =0x8022;
  public static final int GL_POST_CONVOLUTION_ALPHA_BIAS             =0x8023;
  public static final int GL_HISTOGRAM                               =0x8024;
  public static final int GL_PROXY_HISTOGRAM                         =0x8025;
  public static final int GL_HISTOGRAM_WIDTH                         =0x8026;
  public static final int GL_HISTOGRAM_FORMAT                        =0x8027;
  public static final int GL_HISTOGRAM_RED_SIZE                      =0x8028;
  public static final int GL_HISTOGRAM_GREEN_SIZE                    =0x8029;
  public static final int GL_HISTOGRAM_BLUE_SIZE                     =0x802A;
  public static final int GL_HISTOGRAM_ALPHA_SIZE                    =0x802B;
  public static final int GL_HISTOGRAM_LUMINANCE_SIZE                =0x802C;
  public static final int GL_HISTOGRAM_SINK                          =0x802D;
  public static final int GL_MINMAX                                  =0x802E;
  public static final int GL_MINMAX_FORMAT                           =0x802F;
  public static final int GL_MINMAX_SINK                             =0x8030;
  public static final int GL_TABLE_TOO_LARGE                         =0x8031;
  public static final int GL_COLOR_MATRIX                            =0x80B1;
  public static final int GL_COLOR_MATRIX_STACK_DEPTH                =0x80B2;
  public static final int GL_MAX_COLOR_MATRIX_STACK_DEPTH            =0x80B3;
  public static final int GL_POST_COLOR_MATRIX_RED_SCALE             =0x80B4;
  public static final int GL_POST_COLOR_MATRIX_GREEN_SCALE           =0x80B5;
  public static final int GL_POST_COLOR_MATRIX_BLUE_SCALE            =0x80B6;
  public static final int GL_POST_COLOR_MATRIX_ALPHA_SCALE           =0x80B7;
  public static final int GL_POST_COLOR_MATRIX_RED_BIAS              =0x80B8;
  public static final int GL_POST_COLOR_MATRIX_GREEN_BIAS            =0x80B9;
  public static final int GL_POST_COLOR_MATRIX_BLUE_BIAS             =0x80BA;
  public static final int GL_POST_COLOR_MATRIX_ALPHA_BIAS            =0x80BB;
  public static final int GL_COLOR_TABLE                             =0x80D0;
  public static final int GL_POST_CONVOLUTION_COLOR_TABLE            =0x80D1;
  public static final int GL_POST_COLOR_MATRIX_COLOR_TABLE           =0x80D2;
  public static final int GL_PROXY_COLOR_TABLE                       =0x80D3;
  public static final int GL_PROXY_POST_CONVOLUTION_COLOR_TABLE      =0x80D4;
  public static final int GL_PROXY_POST_COLOR_MATRIX_COLOR_TABLE     =0x80D5;
  public static final int GL_COLOR_TABLE_SCALE                       =0x80D6;
  public static final int GL_COLOR_TABLE_BIAS                        =0x80D7;
  public static final int GL_COLOR_TABLE_FORMAT                      =0x80D8;
  public static final int GL_COLOR_TABLE_WIDTH                       =0x80D9;
  public static final int GL_COLOR_TABLE_RED_SIZE                    =0x80DA;
  public static final int GL_COLOR_TABLE_GREEN_SIZE                  =0x80DB;
  public static final int GL_COLOR_TABLE_BLUE_SIZE                   =0x80DC;
  public static final int GL_COLOR_TABLE_ALPHA_SIZE                  =0x80DD;
  public static final int GL_COLOR_TABLE_LUMINANCE_SIZE              =0x80DE;
  public static final int GL_COLOR_TABLE_INTENSITY_SIZE              =0x80DF;
  public static final int GL_CONSTANT_BORDER                         =0x8151;
  public static final int GL_REPLICATE_BORDER                        =0x8153;
  public static final int GL_CONVOLUTION_BORDER_COLOR                =0x8154;

  // OpenGL 1.3
  public static final int GL_TEXTURE0                                =0x84C0;
  public static final int GL_TEXTURE1                                =0x84C1;
  public static final int GL_TEXTURE2                                =0x84C2;
  public static final int GL_TEXTURE3                                =0x84C3;
  public static final int GL_TEXTURE4                                =0x84C4;
  public static final int GL_TEXTURE5                                =0x84C5;
  public static final int GL_TEXTURE6                                =0x84C6;
  public static final int GL_TEXTURE7                                =0x84C7;
  public static final int GL_TEXTURE8                                =0x84C8;
  public static final int GL_TEXTURE9                                =0x84C9;
  public static final int GL_TEXTURE10                               =0x84CA;
  public static final int GL_TEXTURE11                               =0x84CB;
  public static final int GL_TEXTURE12                               =0x84CC;
  public static final int GL_TEXTURE13                               =0x84CD;
  public static final int GL_TEXTURE14                               =0x84CE;
  public static final int GL_TEXTURE15                               =0x84CF;
  public static final int GL_TEXTURE16                               =0x84D0;
  public static final int GL_TEXTURE17                               =0x84D1;
  public static final int GL_TEXTURE18                               =0x84D2;
  public static final int GL_TEXTURE19                               =0x84D3;
  public static final int GL_TEXTURE20                               =0x84D4;
  public static final int GL_TEXTURE21                               =0x84D5;
  public static final int GL_TEXTURE22                               =0x84D6;
  public static final int GL_TEXTURE23                               =0x84D7;
  public static final int GL_TEXTURE24                               =0x84D8;
  public static final int GL_TEXTURE25                               =0x84D9;
  public static final int GL_TEXTURE26                               =0x84DA;
  public static final int GL_TEXTURE27                               =0x84DB;
  public static final int GL_TEXTURE28                               =0x84DC;
  public static final int GL_TEXTURE29                               =0x84DD;
  public static final int GL_TEXTURE30                               =0x84DE;
  public static final int GL_TEXTURE31                               =0x84DF;
  public static final int GL_ACTIVE_TEXTURE                          =0x84E0;
  public static final int GL_CLIENT_ACTIVE_TEXTURE                   =0x84E1;
  public static final int GL_MAX_TEXTURE_UNITS                       =0x84E2;
  public static final int GL_TRANSPOSE_MODELVIEW_MATRIX              =0x84E3;
  public static final int GL_TRANSPOSE_PROJECTION_MATRIX             =0x84E4;
  public static final int GL_TRANSPOSE_TEXTURE_MATRIX                =0x84E5;
  public static final int GL_TRANSPOSE_COLOR_MATRIX                  =0x84E6;
  public static final int GL_MULTISAMPLE                             =0x809D;
  public static final int GL_SAMPLE_ALPHA_TO_COVERAGE                =0x809E;
  public static final int GL_SAMPLE_ALPHA_TO_ONE                     =0x809F;
  public static final int GL_SAMPLE_COVERAGE                         =0x80A0;
  public static final int GL_SAMPLE_BUFFERS                          =0x80A8;
  public static final int GL_SAMPLES                                 =0x80A9;
  public static final int GL_SAMPLE_COVERAGE_VALUE                   =0x80AA;
  public static final int GL_SAMPLE_COVERAGE_INVERT                  =0x80AB;
  public static final int GL_MULTISAMPLE_BIT                     =0x20000000;
  public static final int GL_NORMAL_MAP                              =0x8511;
  public static final int GL_REFLECTION_MAP                          =0x8512;
  public static final int GL_TEXTURE_CUBE_MAP                        =0x8513;
  public static final int GL_TEXTURE_BINDING_CUBE_MAP                =0x8514;
  public static final int GL_TEXTURE_CUBE_MAP_POSITIVE_X             =0x8515;
  public static final int GL_TEXTURE_CUBE_MAP_NEGATIVE_X             =0x8516;
  public static final int GL_TEXTURE_CUBE_MAP_POSITIVE_Y             =0x8517;
  public static final int GL_TEXTURE_CUBE_MAP_NEGATIVE_Y             =0x8518;
  public static final int GL_TEXTURE_CUBE_MAP_POSITIVE_Z             =0x8519;
  public static final int GL_TEXTURE_CUBE_MAP_NEGATIVE_Z             =0x851A;
  public static final int GL_PROXY_TEXTURE_CUBE_MAP                  =0x851B;
  public static final int GL_MAX_CUBE_MAP_TEXTURE_SIZE               =0x851C;
  public static final int GL_COMPRESSED_ALPHA                        =0x84E9;
  public static final int GL_COMPRESSED_LUMINANCE                    =0x84EA;
  public static final int GL_COMPRESSED_LUMINANCE_ALPHA              =0x84EB;
  public static final int GL_COMPRESSED_INTENSITY                    =0x84EC;
  public static final int GL_COMPRESSED_RGB                          =0x84ED;
  public static final int GL_COMPRESSED_RGBA                         =0x84EE;
  public static final int GL_TEXTURE_COMPRESSION_HINT                =0x84EF;
  public static final int GL_TEXTURE_COMPRESSED_IMAGE_SIZE           =0x86A0;
  public static final int GL_TEXTURE_COMPRESSED                      =0x86A1;
  public static final int GL_NUM_COMPRESSED_TEXTURE_FORMATS          =0x86A2;
  public static final int GL_COMPRESSED_TEXTURE_FORMATS              =0x86A3;
  public static final int GL_CLAMP_TO_BORDER                         =0x812D;
  public static final int GL_CLAMP_TO_BORDER_SGIS                    =0x812D;
  public static final int GL_COMBINE                                 =0x8570;
  public static final int GL_COMBINE_RGB                             =0x8571;
  public static final int GL_COMBINE_ALPHA                           =0x8572;
  public static final int GL_SOURCE0_RGB                             =0x8580;
  public static final int GL_SOURCE1_RGB                             =0x8581;
  public static final int GL_SOURCE2_RGB                             =0x8582;
  public static final int GL_SOURCE0_ALPHA                           =0x8588;
  public static final int GL_SOURCE1_ALPHA                           =0x8589;
  public static final int GL_SOURCE2_ALPHA                           =0x858A;
  public static final int GL_OPERAND0_RGB                            =0x8590;
  public static final int GL_OPERAND1_RGB                            =0x8591;
  public static final int GL_OPERAND2_RGB                            =0x8592;
  public static final int GL_OPERAND0_ALPHA                          =0x8598;
  public static final int GL_OPERAND1_ALPHA                          =0x8599;
  public static final int GL_OPERAND2_ALPHA                          =0x859A;
  public static final int GL_RGB_SCALE                               =0x8573;
  public static final int GL_ADD_SIGNED                              =0x8574;
  public static final int GL_INTERPOLATE                             =0x8575;
  public static final int GL_SUBTRACT                                =0x84E7;
  public static final int GL_CONSTANT                                =0x8576;
  public static final int GL_PRIMARY_COLOR                           =0x8577;
  public static final int GL_PREVIOUS                                =0x8578;
  public static final int GL_DOT3_RGB                                =0x86AE;
  public static final int GL_DOT3_RGBA                               =0x86AF;

  // OpenGL 1.4
  public static final int GL_BLEND_DST_RGB                           =0x80C8;
  public static final int GL_BLEND_SRC_RGB                           =0x80C9;
  public static final int GL_BLEND_DST_ALPHA                         =0x80CA;
  public static final int GL_BLEND_SRC_ALPHA                         =0x80CB;
  public static final int GL_POINT_SIZE_MIN                          =0x8126;
  public static final int GL_POINT_SIZE_MAX                          =0x8127;
  public static final int GL_POINT_FADE_THRESHOLD_SIZE               =0x8128;
  public static final int GL_POINT_DISTANCE_ATTENUATION              =0x8129;
  public static final int GL_GENERATE_MIPMAP                         =0x8191;
  public static final int GL_GENERATE_MIPMAP_HINT                    =0x8192;
  public static final int GL_DEPTH_COMPONENT16                       =0x81A5;
  public static final int GL_DEPTH_COMPONENT24                       =0x81A6;
  public static final int GL_DEPTH_COMPONENT32                       =0x81A7;
  public static final int GL_MIRRORED_REPEAT                         =0x8370;
  public static final int GL_FOG_COORDINATE_SOURCE                   =0x8450;
  public static final int GL_FOG_COORDINATE                          =0x8451;
  public static final int GL_FRAGMENT_DEPTH                          =0x8452;
  public static final int GL_CURRENT_FOG_COORDINATE                  =0x8453;
  public static final int GL_FOG_COORDINATE_ARRAY_TYPE               =0x8454;
  public static final int GL_FOG_COORDINATE_ARRAY_STRIDE             =0x8455;
  public static final int GL_FOG_COORDINATE_ARRAY_POINTER            =0x8456;
  public static final int GL_FOG_COORDINATE_ARRAY                    =0x8457;
  public static final int GL_COLOR_SUM                               =0x8458;
  public static final int GL_CURRENT_SECONDARY_COLOR                 =0x8459;
  public static final int GL_SECONDARY_COLOR_ARRAY_SIZE              =0x845A;
  public static final int GL_SECONDARY_COLOR_ARRAY_TYPE              =0x845B;
  public static final int GL_SECONDARY_COLOR_ARRAY_STRIDE            =0x845C;
  public static final int GL_SECONDARY_COLOR_ARRAY_POINTER           =0x845D;
  public static final int GL_SECONDARY_COLOR_ARRAY                   =0x845E;
  public static final int GL_MAX_TEXTURE_LOD_BIAS                    =0x84FD;
  public static final int GL_TEXTURE_FILTER_CONTROL                  =0x8500;
  public static final int GL_TEXTURE_LOD_BIAS                        =0x8501;
  public static final int GL_INCR_WRAP                               =0x8507;
  public static final int GL_DECR_WRAP                               =0x8508;
  public static final int GL_TEXTURE_DEPTH_SIZE                      =0x884A;
  public static final int GL_DEPTH_TEXTURE_MODE                      =0x884B;
  public static final int GL_TEXTURE_COMPARE_MODE                    =0x884C;
  public static final int GL_TEXTURE_COMPARE_FUNC                    =0x884D;
  public static final int GL_COMPARE_R_TO_TEXTURE                    =0x884E;

  // OpenGL 1.5
  public static final int GL_BUFFER_SIZE                             =0x8764;
  public static final int GL_BUFFER_USAGE                            =0x8765;
  public static final int GL_QUERY_COUNTER_BITS                      =0x8864;
  public static final int GL_CURRENT_QUERY                           =0x8865;
  public static final int GL_QUERY_RESULT                            =0x8866;
  public static final int GL_QUERY_RESULT_AVAILABLE                  =0x8867;
  public static final int GL_ARRAY_BUFFER                            =0x8892;
  public static final int GL_ELEMENT_ARRAY_BUFFER                    =0x8893;
  public static final int GL_ARRAY_BUFFER_BINDING                    =0x8894;
  public static final int GL_ELEMENT_ARRAY_BUFFER_BINDING            =0x8895;
  public static final int GL_VERTEX_ARRAY_BUFFER_BINDING             =0x8896;
  public static final int GL_NORMAL_ARRAY_BUFFER_BINDING             =0x8897;
  public static final int GL_COLOR_ARRAY_BUFFER_BINDING              =0x8898;
  public static final int GL_INDEX_ARRAY_BUFFER_BINDING              =0x8899;
  public static final int GL_TEXTURE_COORD_ARRAY_BUFFER_BINDING      =0x889A;
  public static final int GL_EDGE_FLAG_ARRAY_BUFFER_BINDING          =0x889B;
  public static final int GL_SECONDARY_COLOR_ARRAY_BUFFER_BINDING    =0x889C;
  public static final int GL_FOG_COORDINATE_ARRAY_BUFFER_BINDING     =0x889D;
  public static final int GL_WEIGHT_ARRAY_BUFFER_BINDING             =0x889E;
  public static final int GL_VERTEX_ATTRIB_ARRAY_BUFFER_BINDING      =0x889F;
  public static final int GL_READ_ONLY                               =0x88B8;
  public static final int GL_WRITE_ONLY                              =0x88B9;
  public static final int GL_READ_WRITE                              =0x88BA;
  public static final int GL_BUFFER_ACCESS                           =0x88BB;
  public static final int GL_BUFFER_MAPPED                           =0x88BC;
  public static final int GL_BUFFER_MAP_POINTER                      =0x88BD;
  public static final int GL_STREAM_DRAW                             =0x88E0;
  public static final int GL_STREAM_READ                             =0x88E1;
  public static final int GL_STREAM_COPY                             =0x88E2;
  public static final int GL_STATIC_DRAW                             =0x88E4;
  public static final int GL_STATIC_READ                             =0x88E5;
  public static final int GL_STATIC_COPY                             =0x88E6;
  public static final int GL_DYNAMIC_DRAW                            =0x88E8;
  public static final int GL_DYNAMIC_READ                            =0x88E9;
  public static final int GL_DYNAMIC_COPY                            =0x88EA;
  public static final int GL_SAMPLES_PASSED                          =0x8914;
  public static final int GL_FOG_COORD_SOURCE                        =0x8450;
  public static final int GL_FOG_COORD                               =0x8451;
  public static final int GL_CURRENT_FOG_COORD                       =0x8453;
  public static final int GL_FOG_COORD_ARRAY_TYPE                    =0x8454;
  public static final int GL_FOG_COORD_ARRAY_STRIDE                  =0x8455;
  public static final int GL_FOG_COORD_ARRAY_POINTER                 =0x8456;
  public static final int GL_FOG_COORD_ARRAY                         =0x8457;
  public static final int GL_FOG_COORD_ARRAY_BUFFER_BINDING          =0x889D;
  public static final int GL_SRC0_RGB                                =0x8580;
  public static final int GL_SRC1_RGB                                =0x8581;
  public static final int GL_SRC2_RGB                                =0x8582;
  public static final int GL_SRC0_ALPHA                              =0x8588;
  public static final int GL_SRC1_ALPHA                              =0x8589;
  public static final int GL_SRC2_ALPHA                              =0x858A;

  ///////////////////////////////////////////////////////////////////////////
  // methods

  public static native void glAccum(int op, float value);
  public static native void glAlphaFunc(int func, float ref);
  public static native boolean glAreTexturesResident(
    int n, int[] textures, byte[] residences);
  public static native void glArrayElement(int i);
  public static native void glBegin(int mode);
  public static native void glBindTexture(int target, int texture);
  public static native void glBitmap(
    int width, int height, float xorig, float yorig, 
    float xmove, float ymove, byte[] bitmap);
  public static native void glBlendFunc(int sfactor, int dfactor);
  public static native void glCallList(int list);
  public static native void glCallLists(int n, int type, byte[] lists);
  public static void glCallLists(int n, byte[] lists) {
    glCallLists(n,GL_BYTE,lists);
  }
  public static native void glCallLists(int n, int type, int[] lists);
  public static void glCallLists(int n, int[] lists) {
    glCallLists(n,GL_INT,lists);
  }
  public static native void glCallLists(int n, int type, short[] lists);
  public static void glCallLists(int n, short[] lists) {
    glCallLists(n,GL_BYTE,lists);
  }
  public static native void glClear(int mask);
  public static native void glClearAccum(
    float red, float green, float blue, float alpha);
  public static native void glClearColor(
    float red, float green, float blue, float alpha);
  public static native void glClearDepth(double depth);
  public static native void glClearIndex(float c);
  public static native void glClearStencil(int s);
  public static native void glClipPlane(int plane, double[] equation);
  public static native void glColor3b(byte red, byte green, byte blue);
  public static native void glColor3bv(byte[] v);
  public static native void glColor3d(double red, double green, double blue);
  public static native void glColor3dv(double[] v);
  public static native void glColor3f(float red, float green, float blue);
  public static native void glColor3fv(float[] v);
  public static native void glColor3i(int red, int green, int blue);
  public static native void glColor3iv(int[] v);
  public static native void glColor3s(short red, short green, short blue);
  public static native void glColor3sv(short[] v);
  public static native void glColor4b(
    byte red, byte green, byte blue, byte alpha);
  public static native void glColor4bv(byte[] v);
  public static native void glColor4d(
    double red, double green, double blue, double alpha);
  public static native void glColor4dv(double[] v);
  public static native void glColor4f(
    float red, float green, float blue, float alpha);
  public static native void glColor4fv(float[] v);
  public static native void glColor4i(
    int red, int green, int blue, int alpha);
  public static native void glColor4iv(int[] v);
  public static native void glColor4s(
    short red, short green, short blue, short alpha);
  public static native void glColor4sv(short[] v);
  public static native void glColor4ub(
    byte red, byte green, byte blue, byte alpha);
  public static native void glColor4ubv(byte[] v);
  public static native void glColor4ui(
    int red, int green, int blue, int alpha);
  public static native void glColor4uiv(int[] v);
  public static native void glColor4us(
    short red, short green, short blue, short alpha);
  public static native void glColor4usv(short[] v);
  public static native void glColorMask(
    byte red, byte green, byte blue, byte alpha);
  public static native void glColorMaterial(int face, int mode);
  public static native void glColorPointer(
    int size, int type, int stride, Buffer pointer);
  public static native void glCopyPixels(
    int x, int y, int width, int height, int type);
  public static native void glCopyTexImage1D(
    int target, int level, int internalFormat, 
    int x, int y, int width, int border);
  public static native void glCopyTexImage2D(
    int target, int level, int internalFormat, 
    int x, int y, int width, int height, int border);
  public static native void glCopyTexSubImage1D(
    int target, int level, int xoffset, int x, int y, int width);
  public static native void glCopyTexSubImage2D(
    int target, int level, int xoffset, int yoffset, 
    int x, int y, int width, int height);
  public static native void glCullFace(int mode);
  public static native void glDeleteLists(int list, int range);
  public static native void glDeleteTextures(int n, int[] textures);
  public static native void glDepthFunc(int func);
  public static native void glDepthMask(byte flag);
  public static native void glDepthRange(double zNear, double zFar);
  public static native void glDisable(int cap);
  public static native void glDisableClientState(int array);
  public static native void glDrawArrays(int mode, int first, int count);
  public static native void glDrawBuffer(int mode);
  public static native void glDrawElements(
    int mode, int count, int type, byte[] indices);
  public static void glDrawElements(int mode, int count, byte[] indices) {
    glDrawElements(mode,count,GL_UNSIGNED_BYTE,indices);
  }
  public static native void glDrawElements(
    int mode, int count, int type, int[] indices);
  public static void glDrawElements(int mode, int count, int[] indices) {
    glDrawElements(mode,count,GL_UNSIGNED_INT,indices);
  }
  public static native void glDrawElements(
    int mode, int count, int type, short[] indices);
  public static void glDrawElements(int mode, int count, short[] indices) {
    glDrawElements(mode,count,GL_UNSIGNED_SHORT,indices);
  }
  public static native void glDrawPixels(
    int width, int height, int format, int type, byte[] pixels);
  public static native void glDrawPixels(
    int width, int height, int format, int type, int[] pixels);
  public static native void glDrawPixels(
    int width, int height, int format, int type, short[] pixels);
  public static native void glEdgeFlag(byte flag);
  public static native void glEdgeFlagPointer(int stride, Buffer pointer);
  public static native void glEdgeFlagv(byte[] flag);
  public static native void glEnable(int cap);
  public static native void glEnableClientState(int array);
  public static native void glEnd();
  public static native void glEndList();
  public static native void glEvalCoord1d(double u);
  public static native void glEvalCoord1dv(double[] u);
  public static native void glEvalCoord1f(float u);
  public static native void glEvalCoord1fv(float[] u);
  public static native void glEvalCoord2d(double u, double v);
  public static native void glEvalCoord2dv(double[] u);
  public static native void glEvalCoord2f(float u, float v);
  public static native void glEvalCoord2fv(float[] u);
  public static native void glEvalMesh1(int mode, int i1, int i2);
  public static native void glEvalMesh2(
    int mode, int i1, int i2, int j1, int j2);
  public static native void glEvalPoint1(int i);
  public static native void glEvalPoint2(int i, int j);
  public static native void glFeedbackBuffer(
    int size, int type, FloatBuffer buffer);
  public static native void glFinish();
  public static native void glFlush();
  public static native void glFogf(int pname, float param);
  public static native void glFogfv(int pname, float[] params);
  public static native void glFogi(int pname, int param);
  public static native void glFogiv(int pname, int[] params);
  public static native void glFrontFace(int mode);
  public static native void glFrustum(
    double left, double right, 
    double bottom, double top, 
    double zNear, double zFar);
  public static native int glGenLists(int range);
  public static native void glGenTextures(int n, int[] textures);
  public static native void glGetBooleanv(int pname, byte[] params);
  public static native void glGetClipPlane(int plane, double[] equation);
  public static native void glGetDoublev(int pname, double[] params);
  public static native int glGetError();
  public static native void glGetFloatv(int pname, float[] params);
  public static native void glGetIntegerv(int pname, int[] params);
  public static native void glGetLightfv(int light, int pname, float[] params);
  public static native void glGetLightiv(int light, int pname, int[] params);
  public static native void glGetMapdv(int target, int query, double[] v);
  public static native void glGetMapfv(int target, int query, float[] v);
  public static native void glGetMapiv(int target, int query, int[] v);
  public static native void glGetMaterialfv(
    int face, int pname, float[] params);
  public static native void glGetMaterialiv(
    int face, int pname, int[] params);
  public static native void glGetPixelMapfv(int map, float[] values);
  public static native void glGetPixelMapuiv(int map, int[] values);
  public static native void glGetPixelMapusv(int map, short[] values);
//  public static native void glGetPointerv(int pname, GLvoid* *params);
  public static native void glGetPolygonStipple(byte[] mask);
  public static native String glGetString(int name);
  public static native void glGetTexEnvfv(
    int target, int pname, float[] params);
  public static native void glGetTexEnviv(
    int target, int pname, int[] params);
  public static native void glGetTexGendv(
    int coord, int pname, double[] params);
  public static native void glGetTexGenfv(
    int coord, int pname, float[] params);
  public static native void glGetTexGeniv(
    int coord, int pname, int[] params);
  public static native void glGetTexImage(
    int target, int level, int format, int type, byte[] pixels);
  public static native void glGetTexImage(
    int target, int level, int format, int type, int[] pixels);
  public static native void glGetTexImage(
    int target, int level, int format, int type, short[] pixels);
  public static native void glGetTexLevelParameterfv(
    int target, int level, int pname, float[] params);
  public static native void glGetTexLevelParameteriv(
    int target, int level, int pname, int[] params);
  public static native void glGetTexParameterfv(
    int target, int pname, float[] params);
  public static native void glGetTexParameteriv(
    int target, int pname, int[] params);
  public static native void glHint(int target, int mode);
  public static native void glIndexMask(int mask);
  public static native void glIndexPointer(
    int type, int stride, Buffer pointer);
  public static native void glIndexd(double c);
  public static native void glIndexdv(double[] c);
  public static native void glIndexf(float c);
  public static native void glIndexfv(float[] c);
  public static native void glIndexi(int c);
  public static native void glIndexiv(int[] c);
  public static native void glIndexs(short c);
  public static native void glIndexsv(short[] c);
  public static native void glIndexub(byte c);
  public static native void glIndexubv(byte[] c);
  public static native void glInitNames();
  public static native void glInterleavedArrays(
    int format, int stride, Buffer pointer);
  public static native boolean glIsEnabled(int cap);
  public static native boolean glIsList(int list);
  public static native boolean glIsTexture(int texture);
  public static native void glLightModelf(int pname, float param);
  public static native void glLightModelfv(int pname, float[] params);
  public static native void glLightModeli(int pname, int param);
  public static native void glLightModeliv(int pname, int[] params);
  public static native void glLightf(int light, int pname, float param);
  public static native void glLightfv(int light, int pname, float[] params);
  public static native void glLighti(int light, int pname, int param);
  public static native void glLightiv(int light, int pname, int[] params);
  public static native void glLineStipple(int factor, short pattern);
  public static native void glLineWidth(float width);
  public static native void glListBase(int base);
  public static native void glLoadIdentity();
  public static native void glLoadMatrixd(double[] m);
  public static native void glLoadMatrixf(float[] m);
  public static native void glLoadName(int name);
  public static native void glLogicOp(int opcode);
  public static native void glMap1d(
    int target, double u1, double u2, int stride, int order, double[] points);
  public static native void glMap1f(
    int target, float u1, float u2, int stride, int order, float[] points);
  public static native void glMap2d(
    int target, double u1, double u2, int ustride, int uorder, 
    double v1, double v2, int vstride, int vorder, double[] points);
  public static native void glMap2f(
    int target, float u1, float u2, int ustride, int uorder, 
    float v1, float v2, int vstride, int vorder, float[] points);
  public static native void glMapGrid1d(int un, double u1, double u2);
  public static native void glMapGrid1f(int un, float u1, float u2);
  public static native void glMapGrid2d(
    int un, double u1, double u2, int vn, double v1, double v2);
  public static native void glMapGrid2f(
    int un, float u1, float u2, int vn, float v1, float v2);
  public static native void glMaterialf(int face, int pname, float param);
  public static native void glMaterialfv(int face, int pname, float[] params);
  public static native void glMateriali(int face, int pname, int param);
  public static native void glMaterialiv(int face, int pname, int[] params);
  public static native void glMatrixMode(int mode);
  public static native void glMultMatrixd(double[] m);
  public static native void glMultMatrixf(float[] m);
  public static native void glNewList(int list, int mode);
  public static native void glNormal3b(byte nx, byte ny, byte nz);
  public static native void glNormal3bv(byte[] v);
  public static native void glNormal3d(double nx, double ny, double nz);
  public static native void glNormal3dv(double[] v);
  public static native void glNormal3f(float nx, float ny, float nz);
  public static native void glNormal3fv(float[] v);
  public static native void glNormal3i(int nx, int ny, int nz);
  public static native void glNormal3iv(int[] v);
  public static native void glNormal3s(short nx, short ny, short nz);
  public static native void glNormal3sv(short[] v);
  public static native void glNormalPointer(
    int type, int stride, Buffer pointer);
  public static native void glOrtho(
    double left, double right, 
    double bottom, double top, 
    double zNear, double zFar);
  public static native void glPassThrough(float token);
  public static native void glPixelMapfv(int map, int mapsize, float[] values);
  public static native void glPixelMapuiv(int map, int mapsize, int[] values);
  public static native void glPixelMapusv(int map, int mapsize, short[] values);
  public static native void glPixelStoref(int pname, float param);
  public static native void glPixelStorei(int pname, int param);
  public static native void glPixelTransferf(int pname, float param);
  public static native void glPixelTransferi(int pname, int param);
  public static native void glPixelZoom(float xfactor, float yfactor);
  public static native void glPointSize(float size);
  public static native void glPolygonMode(int face, int mode);
  public static native void glPolygonOffset(float factor, float units);
  public static native void glPolygonStipple(byte[] mask);
  public static native void glPopAttrib();
  public static native void glPopClientAttrib();
  public static native void glPopMatrix();
  public static native void glPopName();
  public static native void glPrioritizeTextures(
    int n, int[] textures, float[] priorities);
  public static native void glPushAttrib(int mask);
  public static native void glPushClientAttrib(int mask);
  public static native void glPushMatrix();
  public static native void glPushName(int name);
  public static native void glRasterPos2d(double x, double y);
  public static native void glRasterPos2dv(double[] v);
  public static native void glRasterPos2f(float x, float y);
  public static native void glRasterPos2fv(float[] v);
  public static native void glRasterPos2i(int x, int y);
  public static native void glRasterPos2iv(int[] v);
  public static native void glRasterPos2s(short x, short y);
  public static native void glRasterPos2sv(short[] v);
  public static native void glRasterPos3d(double x, double y, double z);
  public static native void glRasterPos3dv(double[] v);
  public static native void glRasterPos3f(float x, float y, float z);
  public static native void glRasterPos3fv(float[] v);
  public static native void glRasterPos3i(int x, int y, int z);
  public static native void glRasterPos3iv(int[] v);
  public static native void glRasterPos3s(short x, short y, short z);
  public static native void glRasterPos3sv(short[] v);
  public static native void glRasterPos4d(
    double x, double y, double z, double w);
  public static native void glRasterPos4dv(double[] v);
  public static native void glRasterPos4f(float x, float y, float z, float w);
  public static native void glRasterPos4fv(float[] v);
  public static native void glRasterPos4i(int x, int y, int z, int w);
  public static native void glRasterPos4iv(int[] v);
  public static native void glRasterPos4s(short x, short y, short z, short w);
  public static native void glRasterPos4sv(short[] v);
  public static native void glReadBuffer(int mode);
  public static native void glReadPixels(
    int x, int y, int width, int height, int format, int type, byte[] pixels);
  public static native void glReadPixels(
    int x, int y, int width, int height, int format, int type, int[] pixels);
  public static native void glReadPixels(
    int x, int y, int width, int height, int format, int type, short[] pixels);
  public static native void glRectd(double x1, double y1, double x2, double y2);
  public static native void glRectdv(double[] v1, double[] v2);
  public static native void glRectf(float x1, float y1, float x2, float y2);
  public static native void glRectfv(float[] v1, float[] v2);
  public static native void glRecti(int x1, int y1, int x2, int y2);
  public static native void glRectiv(int[] v1, int[] v2);
  public static native void glRects(short x1, short y1, short x2, short y2);
  public static native void glRectsv(short[] v1, short[] v2);
  public static native int glRenderMode(int mode);
  public static native void glRotated(
    double angle, double x, double y, double z);
  public static native void glRotatef(float angle, float x, float y, float z);
  public static native void glScaled(double x, double y, double z);
  public static native void glScalef(float x, float y, float z);
  public static native void glScissor(int x, int y, int width, int height);
  public static native void glSelectBuffer(int size, IntBuffer buffer);
  public static native void glShadeModel(int mode);
  public static native void glStencilFunc(int func, int ref, int mask);
  public static native void glStencilMask(int mask);
  public static native void glStencilOp(int fail, int zfail, int zpass);
  public static native void glTexCoord1d(double s);
  public static native void glTexCoord1dv(double[] v);
  public static native void glTexCoord1f(float s);
  public static native void glTexCoord1fv(float[] v);
  public static native void glTexCoord1i(int s);
  public static native void glTexCoord1iv(int[] v);
  public static native void glTexCoord1s(short s);
  public static native void glTexCoord1sv(short[] v);
  public static native void glTexCoord2d(double s, double t);
  public static native void glTexCoord2dv(double[] v);
  public static native void glTexCoord2f(float s, float t);
  public static native void glTexCoord2fv(float[] v);
  public static native void glTexCoord2i(int s, int t);
  public static native void glTexCoord2iv(int[] v);
  public static native void glTexCoord2s(short s, short t);
  public static native void glTexCoord2sv(short[] v);
  public static native void glTexCoord3d(double s, double t, double r);
  public static native void glTexCoord3dv(double[] v);
  public static native void glTexCoord3f(float s, float t, float r);
  public static native void glTexCoord3fv(float[] v);
  public static native void glTexCoord3i(int s, int t, int r);
  public static native void glTexCoord3iv(int[] v);
  public static native void glTexCoord3s(short s, short t, short r);
  public static native void glTexCoord3sv(short[] v);
  public static native void glTexCoord4d(
    double s, double t, double r, double q);
  public static native void glTexCoord4dv(double[] v);
  public static native void glTexCoord4f(float s, float t, float r, float q);
  public static native void glTexCoord4fv(float[] v);
  public static native void glTexCoord4i(int s, int t, int r, int q);
  public static native void glTexCoord4iv(int[] v);
  public static native void glTexCoord4s(short s, short t, short r, short q);
  public static native void glTexCoord4sv(short[] v);
  public static native void glTexCoordPointer(
    int size, int type, int stride, Buffer pointer);
  public static native void glTexEnvf(int target, int pname, float param);
  public static native void glTexEnvfv(int target, int pname, float[] params);
  public static native void glTexEnvi(int target, int pname, int param);
  public static native void glTexEnviv(int target, int pname, int[] params);
  public static native void glTexGend(int coord, int pname, double param);
  public static native void glTexGendv(int coord, int pname, double[] params);
  public static native void glTexGenf(int coord, int pname, float param);
  public static native void glTexGenfv(int coord, int pname, float[] params);
  public static native void glTexGeni(int coord, int pname, int param);
  public static native void glTexGeniv(int coord, int pname, int[] params);
  public static native void glTexImage1D(
    int target, int level, int internalformat, 
    int width, int border, int format, int type, byte[] pixels);
  public static native void glTexImage1D(
    int target, int level, int internalformat, 
    int width, int border, int format, int type, int[] pixels);
  public static native void glTexImage1D(
    int target, int level, int internalformat, 
    int width, int border, int format, int type, short[] pixels);
  public static native void glTexImage2D(
    int target, int level, int internalformat, 
    int width, int height, int border, int format, int type, byte[] pixels);
  public static native void glTexImage2D(
    int target, int level, int internalformat, 
    int width, int height, int border, int format, int type, int[] pixels);
  public static native void glTexImage2D(
    int target, int level, int internalformat, 
    int width, int height, int border, int format, int type, short[] pixels);
  public static native void glTexParameterf(int target, int pname, float param);
  public static native void glTexParameterfv(
    int target, int pname, float[] params);
  public static native void glTexParameteri(int target, int pname, int param);
  public static native void glTexParameteriv(
    int target, int pname, int[] params);
  public static native void glTexSubImage1D(
    int target, int level, int xoffset, 
    int width, int format, int type, byte[] pixels);
  public static native void glTexSubImage1D(
    int target, int level, int xoffset, 
    int width, int format, int type, int[] pixels);
  public static native void glTexSubImage1D(
    int target, int level, int xoffset, 
    int width, int format, int type, short[] pixels);
  public static native void glTexSubImage2D(
    int target, int level, int xoffset, int yoffset, 
    int width, int height, int format, int type, byte[] pixels);
  public static native void glTexSubImage2D(
    int target, int level, int xoffset, int yoffset, 
    int width, int height, int format, int type, int[] pixels);
  public static native void glTexSubImage2D(
    int target, int level, int xoffset, int yoffset, 
    int width, int height, int format, int type, short[] pixels);
  public static native void glTranslated(double x, double y, double z);
  public static native void glTranslatef(float x, float y, float z);
  public static native void glVertex2d(double x, double y);
  public static native void glVertex2dv(double[] v);
  public static native void glVertex2f(float x, float y);
  public static native void glVertex2fv(float[] v);
  public static native void glVertex2i(int x, int y);
  public static native void glVertex2iv(int[] v);
  public static native void glVertex2s(short x, short y);
  public static native void glVertex2sv(short[] v);
  public static native void glVertex3d(double x, double y, double z);
  public static native void glVertex3dv(double[] v);
  public static native void glVertex3f(float x, float y, float z);
  public static native void glVertex3fv(float[] v);
  public static native void glVertex3i(int x, int y, int z);
  public static native void glVertex3iv(int[] v);
  public static native void glVertex3s(short x, short y, short z);
  public static native void glVertex3sv(short[] v);
  public static native void glVertex4d(double x, double y, double z, double w);
  public static native void glVertex4dv(double[] v);
  public static native void glVertex4f(float x, float y, float z, float w);
  public static native void glVertex4fv(float[] v);
  public static native void glVertex4i(int x, int y, int z, int w);
  public static native void glVertex4iv(int[] v);
  public static native void glVertex4s(short x, short y, short z, short w);
  public static native void glVertex4sv(short[] v);
  public static native void glVertexPointer(
    int size, int type, int stride, Buffer pointer);
  public static native void glViewport(int x, int y, int width, int height);

  ///////////////////////////////////////////////////////////////////////////

  public static void glColor(float red, float green, float blue) {
    glColor3f(red,green,blue);
  }
  public static void glVertex(float x, float y, float z) {
    glVertex3f(x,y,z);
  }

  ///////////////////////////////////////////////////////////////////////////
  // OpenGL 1.2

  public static void glBlendColor(
    float red, float green, float blue, float alpha)
  {
    nglBlendColor(getContext().glBlendColor,
      red,green,blue,alpha);
  }
  public static void glBlendEquation(
    int mode)
  {
    nglBlendEquation(getContext().glBlendEquation,
      mode);
  }
  public static void glDrawRangeElements(
    int mode, int start, int end, int count, int type, byte[] indices)
  {
    nglDrawRangeElements(getContext().glDrawRangeElements,
      mode,start,end,count,type,indices);
  }
  public static void glDrawRangeElements(
    int mode, int start, int end, int count, int type, int[] indices)
  {
    nglDrawRangeElements(getContext().glDrawRangeElements,
      mode,start,end,count,type,indices);
  }
  public static void glDrawRangeElements(
    int mode, int start, int end, int count, int type, short[] indices)
  {
    nglDrawRangeElements(getContext().glDrawRangeElements,
      mode,start,end,count,type,indices);
  }
  public static void glColorTable(
    int target, int internalformat, int width, 
    int format, int type, byte[] table)
  {
    nglColorTable(getContext().glColorTable,
      target,internalformat,width,format,type,table);
  }
  public static void glColorTable(
    int target, int internalformat, int width, 
    int format, int type, int[] table)
  {
    nglColorTable(getContext().glColorTable,
      target,internalformat,width,format,type,table);
  }
  public static void glColorTable(
    int target, int internalformat, int width, 
    int format, int type, short[] table)
  {
    nglColorTable(getContext().glColorTable,
      target,internalformat,width,format,type,table);
  }
  public static void glColorTableParameterfv(
    int target, int pname, float[] params)
  {
    nglColorTableParameterfv(getContext().glColorTableParameterfv,
      target,pname,params);
  }
  public static void glColorTableParameteriv(
    int target, int pname, int[] params)
  {
    nglColorTableParameteriv(getContext().glColorTableParameteriv,
      target,pname,params);
  }
  public static void glCopyColorTable(
    int target, int internalformat, int x, int y, int width)
  {
    nglCopyColorTable(getContext().glCopyColorTable,
      target,internalformat,x,y,width);
  }
  public static void glGetColorTable(
    int target, int format, int type, byte[] table)
  {
    nglGetColorTable(getContext().glGetColorTable,
      target,format,type,table);
  }
  public static void glGetColorTable(
    int target, int format, int type, int[] table)
  {
    nglGetColorTable(getContext().glGetColorTable,
      target,format,type,table);
  }
  public static void glGetColorTable(
    int target, int format, int type, short[] table)
  {
    nglGetColorTable(getContext().glGetColorTable,
      target,format,type,table);
  }
  public static void glGetColorTableParameterfv(
    int target, int pname, float[] params)
  {
    nglGetColorTableParameterfv(getContext().glGetColorTableParameterfv,
      target,pname,params);
  }
  public static void glGetColorTableParameteriv(
    int target, int pname, int[] params)
  {
    nglGetColorTableParameteriv(getContext().glGetColorTableParameteriv,
      target,pname,params);
  }
  public static void glColorSubTable(
    int target, int start, int count, int format, int type, byte[] data)
  {
    nglColorSubTable(getContext().glColorSubTable,
      target,start,count,format,type,data);
  }
  public static void glColorSubTable(
    int target, int start, int count, int format, int type, int[] data)
  {
    nglColorSubTable(getContext().glColorSubTable,
      target,start,count,format,type,data);
  }
  public static void glColorSubTable(
    int target, int start, int count, int format, int type, short[] data)
  {
    nglColorSubTable(getContext().glColorSubTable,
      target,start,count,format,type,data);
  }
  public static void glCopyColorSubTable(
    int target, int start, int x, int y, int width)
  {
    nglCopyColorSubTable(getContext().glCopyColorSubTable,
      target,start,x,y,width);
  }
  public static void glConvolutionFilter1D(
    int target, int internalformat, int width, 
    int format, int type, byte[] image)
  {
    nglConvolutionFilter1D(getContext().glConvolutionFilter1D,
      target,internalformat,width,format,type,image);
  }
  public static void glConvolutionFilter1D(
    int target, int internalformat, int width, 
    int format, int type, float[] image)
  {
    nglConvolutionFilter1D(getContext().glConvolutionFilter1D,
      target,internalformat,width,format,type,image);
  }
  public static void glConvolutionFilter1D(
    int target, int internalformat, int width, 
    int format, int type, int[] image)
  {
    nglConvolutionFilter1D(getContext().glConvolutionFilter1D,
      target,internalformat,width,format,type,image);
  }
  public static void glConvolutionFilter1D(
    int target, int internalformat, int width, 
    int format, int type, short[] image)
  {
    nglConvolutionFilter1D(getContext().glConvolutionFilter1D,
      target,internalformat,width,format,type,image);
  }
  public static void glConvolutionFilter2D(
    int target, int internalformat, int width, int height, 
    int format, int type, byte[] image)
  {
    nglConvolutionFilter2D(getContext().glConvolutionFilter2D,
      target,internalformat,width,height,format,type,image);
  }
  public static void glConvolutionFilter2D(
    int target, int internalformat, int width, int height, 
    int format, int type, float[] image)
  {
    nglConvolutionFilter2D(getContext().glConvolutionFilter2D,
      target,internalformat,width,height,format,type,image);
  }
  public static void glConvolutionFilter2D(
    int target, int internalformat, int width, int height, 
    int format, int type, int[] image)
  {
    nglConvolutionFilter2D(getContext().glConvolutionFilter2D,
      target,internalformat,width,height,format,type,image);
  }
  public static void glConvolutionFilter2D(
    int target, int internalformat, int width, int height, 
    int format, int type, short[] image)
  {
    nglConvolutionFilter2D(getContext().glConvolutionFilter2D,
      target,internalformat,width,height,format,type,image);
  }
  public static void glConvolutionParameterf(
    int target, int pname, float param)
  {
    nglConvolutionParameterf(getContext().glConvolutionParameterf,
      target,pname,param);
  }
  public static void glConvolutionParameterfv(
    int target, int pname, float[] params)
  {
    nglConvolutionParameterfv(getContext().glConvolutionParameterfv,
      target,pname,params);
  }
  public static void glConvolutionParameteri(
    int target, int pname, int param)
  {
    nglConvolutionParameteri(getContext().glConvolutionParameteri,
      target,pname,param);
  }
  public static void glConvolutionParameteriv(
    int target, int pname, int[] params)
  {
    nglConvolutionParameteriv(getContext().glConvolutionParameteriv,
      target,pname,params);
  }
  public static void glCopyConvolutionFilter1D(
    int target, int internalformat, int x, int y, int width)
  {
    nglCopyConvolutionFilter1D(getContext().glCopyConvolutionFilter1D,
      target,internalformat,x,y,width);
  }
  public static void glCopyConvolutionFilter2D(
    int target, int internalformat, int x, int y, int width, int height)
  {
    nglCopyConvolutionFilter2D(getContext().glCopyConvolutionFilter2D,
      target,internalformat,x,y,width,height);
  }
  public static void glGetConvolutionFilter(
    int target, int format, int type, byte[] image)
  {
    nglGetConvolutionFilter(getContext().glGetConvolutionFilter,
      target,format,type,image);
  }
  public static void glGetConvolutionFilter(
    int target, int format, int type, float[] image)
  {
    nglGetConvolutionFilter(getContext().glGetConvolutionFilter,
      target,format,type,image);
  }
  public static void glGetConvolutionFilter(
    int target, int format, int type, int[] image)
  {
    nglGetConvolutionFilter(getContext().glGetConvolutionFilter,
      target,format,type,image);
  }
  public static void glGetConvolutionFilter(
    int target, int format, int type, short[] image)
  {
    nglGetConvolutionFilter(getContext().glGetConvolutionFilter,
      target,format,type,image);
  }
  public static void glGetConvolutionParameterfv(
    int target, int pname, float[] params)
  {
    nglGetConvolutionParameterfv(getContext().glGetConvolutionParameterfv,
      target,pname,params);
  }
  public static void glGetConvolutionParameteriv(
    int target, int pname, int[] params)
  {
    nglGetConvolutionParameteriv(getContext().glGetConvolutionParameteriv,
      target,pname,params);
  }
  public static void glGetSeparableFilter(
    int target, int format, int type, 
    byte[] row, byte[] column, byte[] span)
  {
    nglGetSeparableFilter(getContext().glGetSeparableFilter,
      target,format,type,row,column,span);
  }
  public static void glGetSeparableFilter(
    int target, int format, int type, 
    float[] row, float[] column, float[] span)
  {
    nglGetSeparableFilter(getContext().glGetSeparableFilter,
      target,format,type,row,column,span);
  }
  public static void glGetSeparableFilter(
    int target, int format, int type, 
    int[] row, int[] column, int[] span)
  {
    nglGetSeparableFilter(getContext().glGetSeparableFilter,
      target,format,type,row,column,span);
  }
  public static void glGetSeparableFilter(
    int target, int format, int type, 
    short[] row, short[] column, short[] span)
  {
    nglGetSeparableFilter(getContext().glGetSeparableFilter,
      target,format,type,row,column,span);
  }
  public static void glSeparableFilter2D(
    int target, int internalformat, int width, int height, 
    int format, int type, byte[] row, byte[] column)
  {
    nglSeparableFilter2D(getContext().glSeparableFilter2D,
      target,internalformat,width,height, 
      format,type,row,column);
  }
  public static void glSeparableFilter2D(
    int target, int internalformat, int width, int height, 
    int format, int type, float[] row, float[] column)
  {
    nglSeparableFilter2D(getContext().glSeparableFilter2D,
      target,internalformat,width,height, 
      format,type,row,column);
  }
  public static void glSeparableFilter2D(
    int target, int internalformat, int width, int height, 
    int format, int type, int[] row, int[] column)
  {
    nglSeparableFilter2D(getContext().glSeparableFilter2D,
      target,internalformat,width,height, 
      format,type,row,column);
  }
  public static void glSeparableFilter2D(
    int target, int internalformat, int width, int height, 
    int format, int type, short[] row, short[] column)
  {
    nglSeparableFilter2D(getContext().glSeparableFilter2D,
      target,internalformat,width,height, 
      format,type,row,column);
  }
  public static void glGetHistogram(
    int target, boolean reset, int format, int type, byte[] values)
  {
    nglGetHistogram(getContext().glGetHistogram,
      target,reset,format,type,values);
  }
  public static void glGetHistogram(
    int target, boolean reset, int format, int type, float[] values)
  {
    nglGetHistogram(getContext().glGetHistogram,
      target,reset,format,type,values);
  }
  public static void glGetHistogram(
    int target, boolean reset, int format, int type, int[] values)
  {
    nglGetHistogram(getContext().glGetHistogram,
      target,reset,format,type,values);
  }
  public static void glGetHistogram(
    int target, boolean reset, int format, int type, short[] values)
  {
    nglGetHistogram(getContext().glGetHistogram,
      target,reset,format,type,values);
  }
  public static void glGetHistogramParameterfv(
    int target, int pname, float[] params)
  {
    nglGetHistogramParameterfv(getContext().glGetHistogramParameterfv,
      target,pname,params);
  }
  public static void glGetHistogramParameteriv(
    int target, int pname, int[] params)
  {
    nglGetHistogramParameteriv(getContext().glGetHistogramParameteriv,
      target,pname,params);
  }
  public static void glGetMinmax(
    int target, boolean reset, int format, int type, byte[] values)
  {
    nglGetMinmax(getContext().glGetMinmax,
      target,reset,format,type,values);
  }
  public static void glGetMinmax(
    int target, boolean reset, int format, int type, float[] values)
  {
    nglGetMinmax(getContext().glGetMinmax,
      target,reset,format,type,values);
  }
  public static void glGetMinmax(
    int target, boolean reset, int format, int type, int[] values)
  {
    nglGetMinmax(getContext().glGetMinmax,
      target,reset,format,type,values);
  }
  public static void glGetMinmax(
    int target, boolean reset, int format, int type, short[] values)
  {
    nglGetMinmax(getContext().glGetMinmax,
      target,reset,format,type,values);
  }
  public static void glGetMinmaxParameterfv(
    int target, int pname, float[] params)
  {
    nglGetMinmaxParameterfv(getContext().glGetMinmaxParameterfv,
      target,pname,params);
  }
  public static void glGetMinmaxParameteriv(
    int target, int pname, int[] params)
  {
    nglGetMinmaxParameteriv(getContext().glGetMinmaxParameteriv,
      target,pname,params);
  }
  public static void glHistogram(
    int target, int width, int internalformat, boolean sink)
  {
    nglHistogram(getContext().glHistogram,
      target,width,internalformat,sink);
  }
  public static void glMinmax(
    int target, int internalformat, boolean sink)
  {
    nglMinmax(getContext().glMinmax,
      target,internalformat,sink);
  }
  public static void glResetHistogram(
    int target)
  {
    nglResetHistogram(getContext().glResetHistogram,
      target);
  }
  public static void glResetMinmax(
    int target)
  {
    nglResetMinmax(getContext().glResetMinmax,
      target);
  }
  public static void glTexImage3D(
    int target, int level, int internalformat, int width, int height, 
    int depth, int border, int format, int type, byte[] pixels)
  {
    nglTexImage3D(getContext().glTexImage3D,
      target,level,internalformat,width,height, 
      depth,border,format,type,pixels);
  }
  public static void glTexImage3D(
    int target, int level, int internalformat, int width, int height, 
    int depth, int border, int format, int type, int[] pixels)
  {
    nglTexImage3D(getContext().glTexImage3D,
      target,level,internalformat,width,height, 
      depth,border,format,type,pixels);
  }
  public static void glTexImage3D(
    int target, int level, int internalformat, int width, int height, 
    int depth, int border, int format, int type, short[] pixels)
  {
    nglTexImage3D(getContext().glTexImage3D,
      target,level,internalformat,width,height, 
      depth,border,format,type,pixels);
  }
  public static void glTexSubImage3D(
    int target, int level, int xoffset, int yoffset, int zoffset, 
    int width, int height, int depth, int format, int type, 
    byte[] pixels)
  {
    nglTexSubImage3D(getContext().glTexSubImage3D,
      target,level,xoffset,yoffset,zoffset, 
      width,height,depth,format,type, 
      pixels);
  }
  public static void glTexSubImage3D(
    int target, int level, int xoffset, int yoffset, int zoffset, 
    int width, int height, int depth, int format, int type, 
    int[] pixels)
  {
    nglTexSubImage3D(getContext().glTexSubImage3D,
      target,level,xoffset,yoffset,zoffset, 
      width,height,depth,format,type, 
      pixels);
  }
  public static void glTexSubImage3D(
    int target, int level, int xoffset, int yoffset, int zoffset, 
    int width, int height, int depth, int format, int type, 
    short[] pixels)
  {
    nglTexSubImage3D(getContext().glTexSubImage3D,
      target,level,xoffset,yoffset,zoffset, 
      width,height,depth,format,type, 
      pixels);
  }
  public static void glCopyTexSubImage3D(
    int target, int level, int xoffset, int yoffset, int zoffset, 
    int x, int y, int width, int height)
  {
    nglCopyTexSubImage3D(getContext().glCopyTexSubImage3D,
      target,level,xoffset,yoffset,zoffset, 
      x,y,width,height);
  }
  private static native void nglBlendColor(long pfunc,
    float red, float green, float blue, float alpha);
  private static native void nglBlendEquation(long pfunc,
    int mode);
  private static native void nglDrawRangeElements(long pfunc,
    int mode, int start, int end, int count, int type, byte[] indices);
  private static native void nglDrawRangeElements(long pfunc,
    int mode, int start, int end, int count, int type, int[] indices);
  private static native void nglDrawRangeElements(long pfunc,
    int mode, int start, int end, int count, int type, short[] indices);
  private static native void nglColorTable(long pfunc,
    int target, int internalformat, int width, 
    int format, int type, byte[] table);
  private static native void nglColorTable(long pfunc,
    int target, int internalformat, int width, 
    int format, int type, int[] table);
  private static native void nglColorTable(long pfunc,
    int target, int internalformat, int width, 
    int format, int type, short[] table);
  private static native void nglColorTableParameterfv(long pfunc,
    int target, int pname, float[] params);
  private static native void nglColorTableParameteriv(long pfunc,
    int target, int pname, int[] params);
  private static native void nglCopyColorTable(long pfunc,
    int target, int internalformat, int x, int y, int width);
  private static native void nglGetColorTable(long pfunc,
    int target, int format, int type, byte[] table);
  private static native void nglGetColorTable(long pfunc,
    int target, int format, int type, int[] table);
  private static native void nglGetColorTable(long pfunc,
    int target, int format, int type, short[] table);
  private static native void nglGetColorTableParameterfv(long pfunc,
    int target, int pname, float[] params);
  private static native void nglGetColorTableParameteriv(long pfunc,
    int target, int pname, int[] params);
  private static native void nglColorSubTable(long pfunc,
    int target, int start, int count, int format, int type, byte[] data);
  private static native void nglColorSubTable(long pfunc,
    int target, int start, int count, int format, int type, int[] data);
  private static native void nglColorSubTable(long pfunc,
    int target, int start, int count, int format, int type, short[] data);
  private static native void nglCopyColorSubTable(long pfunc,
    int target, int start, int x, int y, int width);
  private static native void nglConvolutionFilter1D(long pfunc,
    int target, int internalformat, int width, 
    int format, int type, byte[] image);
  private static native void nglConvolutionFilter1D(long pfunc,
    int target, int internalformat, int width, 
    int format, int type, float[] image);
  private static native void nglConvolutionFilter1D(long pfunc,
    int target, int internalformat, int width, 
    int format, int type, int[] image);
  private static native void nglConvolutionFilter1D(long pfunc,
    int target, int internalformat, int width, 
    int format, int type, short[] image);
  private static native void nglConvolutionFilter2D(long pfunc,
    int target, int internalformat, int width, int height, 
    int format, int type, byte[] image);
  private static native void nglConvolutionFilter2D(long pfunc,
    int target, int internalformat, int width, int height, 
    int format, int type, float[] image);
  private static native void nglConvolutionFilter2D(long pfunc,
    int target, int internalformat, int width, int height, 
    int format, int type, int[] image);
  private static native void nglConvolutionFilter2D(long pfunc,
    int target, int internalformat, int width, int height, 
    int format, int type, short[] image);
  private static native void nglConvolutionParameterf(long pfunc,
    int target, int pname, float param);
  private static native void nglConvolutionParameterfv(long pfunc,
    int target, int pname, float[] params);
  private static native void nglConvolutionParameteri(long pfunc,
    int target, int pname, int param);
  private static native void nglConvolutionParameteriv(long pfunc,
    int target, int pname, int[] params);
  private static native void nglCopyConvolutionFilter1D(long pfunc,
    int target, int internalformat, int x, int y, int width);
  private static native void nglCopyConvolutionFilter2D(long pfunc,
    int target, int internalformat, int x, int y, int width, int height);
  private static native void nglGetConvolutionFilter(long pfunc,
    int target, int format, int type, byte[] image);
  private static native void nglGetConvolutionFilter(long pfunc,
    int target, int format, int type, float[] image);
  private static native void nglGetConvolutionFilter(long pfunc,
    int target, int format, int type, int[] image);
  private static native void nglGetConvolutionFilter(long pfunc,
    int target, int format, int type, short[] image);
  private static native void nglGetConvolutionParameterfv(long pfunc,
    int target, int pname, float[] params);
  private static native void nglGetConvolutionParameteriv(long pfunc,
    int target, int pname, int[] params);
  private static native void nglGetSeparableFilter(long pfunc,
    int target, int format, int type, 
    byte[] row, byte[] column, byte[] span);
  private static native void nglGetSeparableFilter(long pfunc,
    int target, int format, int type, 
    float[] row, float[] column, float[] span);
  private static native void nglGetSeparableFilter(long pfunc,
    int target, int format, int type, 
    int[] row, int[] column, int[] span);
  private static native void nglGetSeparableFilter(long pfunc,
    int target, int format, int type, 
    short[] row, short[] column, short[] span);
  private static native void nglSeparableFilter2D(long pfunc,
    int target, int internalformat, int width, int height, 
    int format, int type, byte[] row, byte[] column);
  private static native void nglSeparableFilter2D(long pfunc,
    int target, int internalformat, int width, int height, 
    int format, int type, float[] row, float[] column);
  private static native void nglSeparableFilter2D(long pfunc,
    int target, int internalformat, int width, int height, 
    int format, int type, int[] row, int[] column);
  private static native void nglSeparableFilter2D(long pfunc,
    int target, int internalformat, int width, int height, 
    int format, int type, short[] row, short[] column);
  private static native void nglGetHistogram(long pfunc,
    int target, boolean reset, int format, int type, byte[] values);
  private static native void nglGetHistogram(long pfunc,
    int target, boolean reset, int format, int type, float[] values);
  private static native void nglGetHistogram(long pfunc,
    int target, boolean reset, int format, int type, int[] values);
  private static native void nglGetHistogram(long pfunc,
    int target, boolean reset, int format, int type, short[] values);
  private static native void nglGetHistogramParameterfv(long pfunc,
    int target, int pname, float[] params);
  private static native void nglGetHistogramParameteriv(long pfunc,
    int target, int pname, int[] params);
  private static native void nglGetMinmax(long pfunc,
    int target, boolean reset, int format, int type, byte[] values);
  private static native void nglGetMinmax(long pfunc,
    int target, boolean reset, int format, int type, float[] values);
  private static native void nglGetMinmax(long pfunc,
    int target, boolean reset, int format, int type, int[] values);
  private static native void nglGetMinmax(long pfunc,
    int target, boolean reset, int format, int type, short[] values);
  private static native void nglGetMinmaxParameterfv(long pfunc,
    int target, int pname, float[] params);
  private static native void nglGetMinmaxParameteriv(long pfunc,
    int target, int pname, int[] params);
  private static native void nglHistogram(long pfunc,
    int target, int width, int internalformat, boolean sink);
  private static native void nglMinmax(long pfunc,
    int target, int internalformat, boolean sink);
  private static native void nglResetHistogram(long pfunc,
    int target);
  private static native void nglResetMinmax(long pfunc,
    int target);
  private static native void nglTexImage3D(long pfunc,
    int target, int level, int internalformat, int width, int height, 
    int depth, int border, int format, int type, byte[] pixels);
  private static native void nglTexImage3D(long pfunc,
    int target, int level, int internalformat, int width, int height, 
    int depth, int border, int format, int type, int[] pixels);
  private static native void nglTexImage3D(long pfunc,
    int target, int level, int internalformat, int width, int height, 
    int depth, int border, int format, int type, short[] pixels);
  private static native void nglTexSubImage3D(long pfunc,
    int target, int level, int xoffset, int yoffset, int zoffset, 
    int width, int height, int depth, int format, int type, 
    byte[] pixels);
  private static native void nglTexSubImage3D(long pfunc,
    int target, int level, int xoffset, int yoffset, int zoffset, 
    int width, int height, int depth, int format, int type, 
    int[] pixels);
  private static native void nglTexSubImage3D(long pfunc,
    int target, int level, int xoffset, int yoffset, int zoffset, 
    int width, int height, int depth, int format, int type, 
    short[] pixels);
  private static native void nglCopyTexSubImage3D(long pfunc,
    int target, int level, int xoffset, int yoffset, int zoffset, 
    int x, int y, int width, int height);

  ///////////////////////////////////////////////////////////////////////////
  // OpenGL 1.3

  public static void glActiveTexture(int texture)
  {
    nglActiveTexture(getContext().glActiveTexture,
      texture);
  }
  public static void glClientActiveTexture(int texture)
  {
    nglClientActiveTexture(getContext().glClientActiveTexture,
      texture);
  }
  public static void glMultiTexCoord1d(int target, double s)
  {
    nglMultiTexCoord1d(getContext().glMultiTexCoord1d,
      target,s);
  }
  public static void glMultiTexCoord1dv(int target, double[] v)
  {
    nglMultiTexCoord1dv(getContext().glMultiTexCoord1dv,
      target,v);
  }
  public static void glMultiTexCoord1f(int target, float s)
  {
    nglMultiTexCoord1f(getContext().glMultiTexCoord1f,
      target,s);
  }
  public static void glMultiTexCoord1fv(int target, float[] v)
  {
    nglMultiTexCoord1fv(getContext().glMultiTexCoord1fv,
      target,v);
  }
  public static void glMultiTexCoord1i(int target, int s)
  {
    nglMultiTexCoord1i(getContext().glMultiTexCoord1i,
      target,s);
  }
  public static void glMultiTexCoord1iv(int target, int[] v)
  {
    nglMultiTexCoord1iv(getContext().glMultiTexCoord1iv,
      target,v);
  }
  public static void glMultiTexCoord1s(int target, short s)
  {
    nglMultiTexCoord1s(getContext().glMultiTexCoord1s,
      target,s);
  }
  public static void glMultiTexCoord1sv(int target, short[] v)
  {
    nglMultiTexCoord1sv(getContext().glMultiTexCoord1sv,
      target,v);
  }
  public static void glMultiTexCoord2d(int target, double s, double t)
  {
    nglMultiTexCoord2d(getContext().glMultiTexCoord2d,
      target,s,t);
  }
  public static void glMultiTexCoord2dv(int target, double[] v)
  {
    nglMultiTexCoord2dv(getContext().glMultiTexCoord2dv,
      target,v);
  }
  public static void glMultiTexCoord2f(int target, float s, float t)
  {
    nglMultiTexCoord2f(getContext().glMultiTexCoord2f,
      target,s,t);
  }
  public static void glMultiTexCoord2fv(int target, float[] v)
  {
    nglMultiTexCoord2fv(getContext().glMultiTexCoord2fv,
      target,v);
  }
  public static void glMultiTexCoord2i(int target, int s, int t)
  {
    nglMultiTexCoord2i(getContext().glMultiTexCoord2i,
      target,s,t);
  }
  public static void glMultiTexCoord2iv(int target, int[] v)
  {
    nglMultiTexCoord2iv(getContext().glMultiTexCoord2iv,
      target,v);
  }
  public static void glMultiTexCoord2s(int target, short s, short t)
  {
    nglMultiTexCoord2s(getContext().glMultiTexCoord2s,
      target,s,t);
  }
  public static void glMultiTexCoord2sv(int target, short[] v)
  {
    nglMultiTexCoord2sv(getContext().glMultiTexCoord2sv,
      target,v);
  }
  public static void glMultiTexCoord3d(
    int target, double s, double t, double r)
  {
    nglMultiTexCoord3d(getContext().glMultiTexCoord3d,
      target,s,t,r);
  }
  public static void glMultiTexCoord3dv(int target, double[] v)
  {
    nglMultiTexCoord3dv(getContext().glMultiTexCoord3dv,
      target,v);
  }
  public static void glMultiTexCoord3f(
    int target, float s, float t, float r)
  {
    nglMultiTexCoord3f(getContext().glMultiTexCoord3f,
      target,s,t,r);
  }
  public static void glMultiTexCoord3fv(int target, float[] v)
  {
    nglMultiTexCoord3fv(getContext().glMultiTexCoord3fv,
      target,v);
  }
  public static void glMultiTexCoord3i(
    int target, int s, int t, int r)
  {
    nglMultiTexCoord3i(getContext().glMultiTexCoord3i,
      target,s,t,r);
  }
  public static void glMultiTexCoord3iv(int target, int[] v)
  {
    nglMultiTexCoord3iv(getContext().glMultiTexCoord3iv,
      target,v);
  }
  public static void glMultiTexCoord3s(
    int target, short s, short t, short r)
  {
    nglMultiTexCoord3s(getContext().glMultiTexCoord3s,
      target,s,t,r);
  }
  public static void glMultiTexCoord3sv(int target, short[] v)
  {
    nglMultiTexCoord3sv(getContext().glMultiTexCoord3sv,
      target,v);
  }
  public static void glMultiTexCoord4d(
    int target, double s, double t, double r, double q)
  {
    nglMultiTexCoord4d(getContext().glMultiTexCoord4d,
      target,s,t,r,q);
  }
  public static void glMultiTexCoord4dv(int target, double[] v)
  {
    nglMultiTexCoord4dv(getContext().glMultiTexCoord4dv,
      target,v);
  }
  public static void glMultiTexCoord4f(
    int target, float s, float t, float r, float q)
  {
    nglMultiTexCoord4f(getContext().glMultiTexCoord4f,
      target,s,t,r,q);
  }
  public static void glMultiTexCoord4fv(int target, float[] v)
  {
    nglMultiTexCoord4fv(getContext().glMultiTexCoord4fv,
      target,v);
  }
  public static void glMultiTexCoord4i(
    int target, int s, int t, int r, int q)
  {
    nglMultiTexCoord4i(getContext().glMultiTexCoord4i,
      target,s,t,r,q);
  }
  public static void glMultiTexCoord4iv(int target, int[] v)
  {
    nglMultiTexCoord4iv(getContext().glMultiTexCoord4iv,
      target,v);
  }
  public static void glMultiTexCoord4s(
    int target, short s, short t, short r, short q)
  {
    nglMultiTexCoord4s(getContext().glMultiTexCoord4s,
      target,s,t,r,q);
  }
  public static void glMultiTexCoord4sv(int target, short[] v)
  {
    nglMultiTexCoord4sv(getContext().glMultiTexCoord4sv,
      target,v);
  }
  public static void glLoadTransposeMatrixf(float[] m)
  {
    nglLoadTransposeMatrixf(getContext().glLoadTransposeMatrixf,
      m);
  }
  public static void glLoadTransposeMatrixd(double[] m)
  {
    nglLoadTransposeMatrixd(getContext().glLoadTransposeMatrixd,
      m);
  }
  public static void glMultTransposeMatrixf(float[] m)
  {
    nglMultTransposeMatrixf(getContext().glMultTransposeMatrixf,
      m);
  }
  public static void glMultTransposeMatrixd(double[] m)
  {
    nglMultTransposeMatrixd(getContext().glMultTransposeMatrixd,
      m);
  }
  public static void glSampleCoverage(float value, boolean invert)
  {
    nglSampleCoverage(getContext().glSampleCoverage,
      value,invert);
  }
  public static void glCompressedTexImage3D(
    int target, int level, int internalformat,
    int width, int height, int depth, int border, int imageSize, byte[] data)
  {
    nglCompressedTexImage3D(getContext().glCompressedTexImage3D,
      target,level,internalformat,
      width,height,depth,border,imageSize,data);
  }
  public static void glCompressedTexImage3D(
    int target, int level, int internalformat,
    int width, int height, int depth, int border, int imageSize, int[] data)
  {
    nglCompressedTexImage3D(getContext().glCompressedTexImage3D,
      target,level,internalformat,
      width,height,depth,border,imageSize,data);
  }
  public static void glCompressedTexImage3D(
    int target, int level, int internalformat,
    int width, int height, int depth, int border, int imageSize, short[] data)
  {
    nglCompressedTexImage3D(getContext().glCompressedTexImage3D,
      target,level,internalformat,
      width,height,depth,border,imageSize,data);
  }
  public static void glCompressedTexImage2D(
    int target, int level, int internalformat,
    int width, int height, int border, int imageSize, byte[] data)
  {
    nglCompressedTexImage2D(getContext().glCompressedTexImage2D,
      target,level,internalformat,
      width,height,border,imageSize,data);
  }
  public static void glCompressedTexImage2D(
    int target, int level, int internalformat,
    int width, int height, int border, int imageSize, int[] data)
  {
    nglCompressedTexImage2D(getContext().glCompressedTexImage2D,
      target,level,internalformat,
      width,height,border,imageSize,data);
  }
  public static void glCompressedTexImage2D(
    int target, int level, int internalformat,
    int width, int height, int border, int imageSize, short[] data)
  {
    nglCompressedTexImage2D(getContext().glCompressedTexImage2D,
      target,level,internalformat,
      width,height,border,imageSize,data);
  }
  public static void glCompressedTexImage1D(
    int target, int level, int internalformat,
    int width, int border, int imageSize, byte[] data)
  {
    nglCompressedTexImage1D(getContext().glCompressedTexImage1D,
      target,level,internalformat,
      width,border,imageSize,data);
  }
  public static void glCompressedTexImage1D(
    int target, int level, int internalformat,
    int width, int border, int imageSize, int[] data)
  {
    nglCompressedTexImage1D(getContext().glCompressedTexImage1D,
      target,level,internalformat,
      width,border,imageSize,data);
  }
  public static void glCompressedTexImage1D(
    int target, int level, int internalformat,
    int width, int border, int imageSize, short[] data)
  {
    nglCompressedTexImage1D(getContext().glCompressedTexImage1D,
      target,level,internalformat,
      width,border,imageSize,data);
  }
  public static void glCompressedTexSubImage3D(
    int target, int level, int xoffset, int yoffset, int zoffset, 
    int width, int height, int depth, int format, int imageSize, byte[] data)
  {
    nglCompressedTexSubImage3D(getContext().glCompressedTexSubImage3D,
      target,level,xoffset,yoffset,zoffset,
      width,height,depth,format,imageSize,data);
  }
  public static void glCompressedTexSubImage3D(
    int target, int level, int xoffset, int yoffset, int zoffset, 
    int width, int height, int depth, int format, int imageSize, int[] data)
  {
    nglCompressedTexSubImage3D(getContext().glCompressedTexSubImage3D,
      target,level,xoffset,yoffset,zoffset,
      width,height,depth,format,imageSize,data);
  }
  public static void glCompressedTexSubImage3D(
    int target, int level, int xoffset, int yoffset, int zoffset, 
    int width, int height, int depth, int format, int imageSize, short[] data)
  {
    nglCompressedTexSubImage3D(getContext().glCompressedTexSubImage3D,
      target,level,xoffset,yoffset,zoffset,
      width,height,depth,format,imageSize,data);
  }
  public static void glCompressedTexSubImage2D(
    int target, int level, int xoffset, int yoffset, 
    int width, int height, int format, int imageSize, byte[] data)
  {
    nglCompressedTexSubImage2D(getContext().glCompressedTexSubImage2D,
      target,level,xoffset,yoffset,
      width,height,format,imageSize,data);
  }
  public static void glCompressedTexSubImage2D(
    int target, int level, int xoffset, int yoffset, 
    int width, int height, int format, int imageSize, int[] data)
  {
    nglCompressedTexSubImage2D(getContext().glCompressedTexSubImage2D,
      target,level,xoffset,yoffset,
      width,height,format,imageSize,data);
  }
  public static void glCompressedTexSubImage2D(
    int target, int level, int xoffset, int yoffset, 
    int width, int height, int format, int imageSize, short[] data)
  {
    nglCompressedTexSubImage2D(getContext().glCompressedTexSubImage2D,
      target,level,xoffset,yoffset,
      width,height,format,imageSize,data);
  }
  public static void glCompressedTexSubImage1D(
    int target, int level, int xoffset, 
    int width, int format, int imageSize, byte[] data)
  {
    nglCompressedTexSubImage1D(getContext().glCompressedTexSubImage1D,
      target,level,xoffset,
      width,format,imageSize,data);
  }
  public static void glCompressedTexSubImage1D(
    int target, int level, int xoffset, 
    int width, int format, int imageSize, int[] data)
  {
    nglCompressedTexSubImage1D(getContext().glCompressedTexSubImage1D,
      target,level,xoffset,
      width,format,imageSize,data);
  }
  public static void glCompressedTexSubImage1D(
    int target, int level, int xoffset, 
    int width, int format, int imageSize, short[] data)
  {
    nglCompressedTexSubImage1D(getContext().glCompressedTexSubImage1D,
      target,level,xoffset,
      width,format,imageSize,data);
  }
  public static void glGetCompressedTexImage(
    int target, int level, byte[] img)
  {
    nglGetCompressedTexImage(getContext().glGetCompressedTexImage,
      target,level,img);
  }
  public static void glGetCompressedTexImage(
    int target, int level, int[] img)
  {
    nglGetCompressedTexImage(getContext().glGetCompressedTexImage,
      target,level,img);
  }
  public static void glGetCompressedTexImage(
    int target, int level, short[] img)
  {
    nglGetCompressedTexImage(getContext().glGetCompressedTexImage,
      target,level,img);
  }
  private static native void nglActiveTexture(long pfunc,
    int texture);
  private static native void nglClientActiveTexture(long pfunc,
    int texture);
  private static native void nglMultiTexCoord1d(long pfunc,
    int target, double s);
  private static native void nglMultiTexCoord1dv(long pfunc,
    int target, double[] v);
  private static native void nglMultiTexCoord1f(long pfunc,
    int target, float s);
  private static native void nglMultiTexCoord1fv(long pfunc,
    int target, float[] v);
  private static native void nglMultiTexCoord1i(long pfunc,
    int target, int s);
  private static native void nglMultiTexCoord1iv(long pfunc,
    int target, int[] v);
  private static native void nglMultiTexCoord1s(long pfunc,
    int target, short s);
  private static native void nglMultiTexCoord1sv(long pfunc,
    int target, short[] v);
  private static native void nglMultiTexCoord2d(long pfunc,
    int target, double s, double t);
  private static native void nglMultiTexCoord2dv(long pfunc,
    int target, double[] v);
  private static native void nglMultiTexCoord2f(long pfunc,
    int target, float s, float t);
  private static native void nglMultiTexCoord2fv(long pfunc,
    int target, float[] v);
  private static native void nglMultiTexCoord2i(long pfunc,
    int target, int s, int t);
  private static native void nglMultiTexCoord2iv(long pfunc,
    int target, int[] v);
  private static native void nglMultiTexCoord2s(long pfunc,
    int target, short s, short t);
  private static native void nglMultiTexCoord2sv(long pfunc,
    int target, short[] v);
  private static native void nglMultiTexCoord3d(long pfunc,
    int target, double s, double t, double r);
  private static native void nglMultiTexCoord3dv(long pfunc,
    int target, double[] v);
  private static native void nglMultiTexCoord3f(long pfunc,
    int target, float s, float t, float r);
  private static native void nglMultiTexCoord3fv(long pfunc,
    int target, float[] v);
  private static native void nglMultiTexCoord3i(long pfunc,
    int target, int s, int t, int r);
  private static native void nglMultiTexCoord3iv(long pfunc,
    int target, int[] v);
  private static native void nglMultiTexCoord3s(long pfunc,
    int target, short s, short t, short r);
  private static native void nglMultiTexCoord3sv(long pfunc,
    int target, short[] v);
  private static native void nglMultiTexCoord4d(long pfunc,
    int target, double s, double t, double r, double q);
  private static native void nglMultiTexCoord4dv(long pfunc,
    int target, double[] v);
  private static native void nglMultiTexCoord4f(long pfunc,
    int target, float s, float t, float r, float q);
  private static native void nglMultiTexCoord4fv(long pfunc,
    int target, float[] v);
  private static native void nglMultiTexCoord4i(long pfunc,
    int target, int s, int t, int r, int q);
  private static native void nglMultiTexCoord4iv(long pfunc,
    int target, int[] v);
  private static native void nglMultiTexCoord4s(long pfunc,
    int target, short s, short t, short r, short q);
  private static native void nglMultiTexCoord4sv(long pfunc,
    int target, short[] v);
  private static native void nglLoadTransposeMatrixf(long pfunc,
    float[] m);
  private static native void nglLoadTransposeMatrixd(long pfunc,
    double[] m);
  private static native void nglMultTransposeMatrixf(long pfunc,
    float[] m);
  private static native void nglMultTransposeMatrixd(long pfunc,
    double[] m);
  private static native void nglSampleCoverage(long pfunc,
    float value, boolean invert);
  private static native void nglCompressedTexImage3D(long pfunc,
    int target, int level, int internalformat,
    int width, int height, int depth, int border, int imageSize, byte[] data);
  private static native void nglCompressedTexImage3D(long pfunc,
    int target, int level, int internalformat,
    int width, int height, int depth, int border, int imageSize, int[] data);
  private static native void nglCompressedTexImage3D(long pfunc,
    int target, int level, int internalformat,
    int width, int height, int depth, int border, int imageSize, short[] data);
  private static native void nglCompressedTexImage2D(long pfunc,
    int target, int level, int internalformat,
    int width, int height, int border, int imageSize, byte[] data);
  private static native void nglCompressedTexImage2D(long pfunc,
    int target, int level, int internalformat,
    int width, int height, int border, int imageSize, int[] data);
  private static native void nglCompressedTexImage2D(long pfunc,
    int target, int level, int internalformat,
    int width, int height, int border, int imageSize, short[] data);
  private static native void nglCompressedTexImage1D(long pfunc,
    int target, int level, int internalformat,
    int width, int border, int imageSize, byte[] data);
  private static native void nglCompressedTexImage1D(long pfunc,
    int target, int level, int internalformat,
    int width, int border, int imageSize, int[] data);
  private static native void nglCompressedTexImage1D(long pfunc,
    int target, int level, int internalformat,
    int width, int border, int imageSize, short[] data);
  private static native void nglCompressedTexSubImage3D(long pfunc,
    int target, int level, int xoffset, int yoffset, int zoffset, 
    int width, int height, int depth, int format, int imageSize, byte[] data);
  private static native void nglCompressedTexSubImage3D(long pfunc,
    int target, int level, int xoffset, int yoffset, int zoffset, 
    int width, int height, int depth, int format, int imageSize, int[] data);
  private static native void nglCompressedTexSubImage3D(long pfunc,
    int target, int level, int xoffset, int yoffset, int zoffset, 
    int width, int height, int depth, int format, int imageSize, short[] data);
  private static native void nglCompressedTexSubImage2D(long pfunc,
    int target, int level, int xoffset, int yoffset, 
    int width, int height, int format, int imageSize, byte[] data);
  private static native void nglCompressedTexSubImage2D(long pfunc,
    int target, int level, int xoffset, int yoffset, 
    int width, int height, int format, int imageSize, int[] data);
  private static native void nglCompressedTexSubImage2D(long pfunc,
    int target, int level, int xoffset, int yoffset, 
    int width, int height, int format, int imageSize, short[] data);
  private static native void nglCompressedTexSubImage1D(long pfunc,
    int target, int level, int xoffset, 
    int width, int format, int imageSize, byte[] data);
  private static native void nglCompressedTexSubImage1D(long pfunc,
    int target, int level, int xoffset, 
    int width, int format, int imageSize, int[] data);
  private static native void nglCompressedTexSubImage1D(long pfunc,
    int target, int level, int xoffset, 
    int width, int format, int imageSize, short[] data);
  private static native void nglGetCompressedTexImage(long pfunc,
    int target, int level, byte[] img);
  private static native void nglGetCompressedTexImage(long pfunc,
    int target, int level, int[] img);
  private static native void nglGetCompressedTexImage(long pfunc,
    int target, int level, short[] img);

  ///////////////////////////////////////////////////////////////////////////
  // OpenGL 1.4

  public static void glBlendFuncSeparate(
    int sfactorRGB, int dfactorRGB, int sfactorAlpha, int dfactorAlpha)
  {
    nglBlendFuncSeparate(getContext().glBlendFuncSeparate,
      sfactorRGB,dfactorRGB,sfactorAlpha,dfactorAlpha);
  }
  public static void glFogCoordf(
    float coord)
  {
    nglFogCoordf(getContext().glFogCoordf,
      coord);
  }
  public static void glFogCoordfv(
    float[] coord)
  {
    nglFogCoordfv(getContext().glFogCoordfv,
      coord);
  }
  public static void glFogCoordd(
    double coord)
  {
    nglFogCoordd(getContext().glFogCoordd,
      coord);
  }
  public static void glFogCoorddv(
    double[] coord)
  {
    nglFogCoorddv(getContext().glFogCoorddv,
      coord);
  }
  public static void glFogCoordPointer(
    int type, int stride, Buffer pointer)
  {
    nglFogCoordPointer(getContext().glFogCoordPointer,
      type,stride,pointer);
  }
  public static void glMultiDrawArrays(
    int mode, int[] first, int[] count, int primcount)
  {
    nglMultiDrawArrays(getContext().glMultiDrawArrays,
      mode,first,count,primcount);
  }
  public static void glMultiDrawElements(
    int mode, int[] count, int type, byte[][] indices, int primcount)
  {
    nglMultiDrawElements(getContext().glMultiDrawElements,
      mode,count,type,indices,primcount);
  }
  public static void glMultiDrawElements(
    int mode, int[] count, int type, int[][] indices, int primcount)
  {
    nglMultiDrawElements(getContext().glMultiDrawElements,
      mode,count,type,indices,primcount);
  }
  public static void glMultiDrawElements(
    int mode, int[] count, int type, short[][] indices, int primcount)
  {
    nglMultiDrawElements(getContext().glMultiDrawElements,
      mode,count,type,indices,primcount);
  }
  public static void glPointParameterf(
    int pname, float param)
  {
    nglPointParameterf(getContext().glPointParameterf,
      pname,param);
  }
  public static void glPointParameterfv(
    int pname, float[] params)
  {
    nglPointParameterfv(getContext().glPointParameterfv,
      pname,params);
  }
  public static void glPointParameteri(
    int pname, int param)
  {
    nglPointParameteri(getContext().glPointParameteri,
      pname,param);
  }
  public static void glPointParameteriv(
    int pname, int[] params)
  {
    nglPointParameteriv(getContext().glPointParameteriv,
      pname,params);
  }
  public static void glSecondaryColor3b(
    byte red, byte green, byte blue)
  {
    nglSecondaryColor3b(getContext().glSecondaryColor3b,
      red,green,blue);
  }
  public static void glSecondaryColor3bv(
    byte[] v)
  {
    nglSecondaryColor3bv(getContext().glSecondaryColor3bv,
      v);
  }
  public static void glSecondaryColor3d(
    double red, double green, double blue)
  {
    nglSecondaryColor3d(getContext().glSecondaryColor3d,
      red,green,blue);
  }
  public static void glSecondaryColor3dv(
    double[] v)
  {
    nglSecondaryColor3dv(getContext().glSecondaryColor3dv,
      v);
  }
  public static void glSecondaryColor3f(
    float red, float green, float blue)
  {
    nglSecondaryColor3f(getContext().glSecondaryColor3f,
      red,green,blue);
  }
  public static void glSecondaryColor3fv(
    float[] v)
  {
    nglSecondaryColor3fv(getContext().glSecondaryColor3fv,
      v);
  }
  public static void glSecondaryColor3i(
    int red, int green, int blue)
  {
    nglSecondaryColor3i(getContext().glSecondaryColor3i,
      red,green,blue);
  }
  public static void glSecondaryColor3iv(
    int[] v)
  {
    nglSecondaryColor3iv(getContext().glSecondaryColor3iv,
      v);
  }
  public static void glSecondaryColor3s(
    short red, short green, short blue)
  {
    nglSecondaryColor3s(getContext().glSecondaryColor3s,
      red,green,blue);
  }
  public static void glSecondaryColor3sv(
    short[] v)
  {
    nglSecondaryColor3sv(getContext().glSecondaryColor3sv,
      v);
  }
  public static void glSecondaryColor3ub(
    byte red, byte green, byte blue)
  {
    nglSecondaryColor3ub(getContext().glSecondaryColor3ub,
      red,green,blue);
  }
  public static void glSecondaryColor3ubv(
    byte[] v)
  {
    nglSecondaryColor3ubv(getContext().glSecondaryColor3ubv,
      v);
  }
  public static void glSecondaryColor3ui(
    int red, int green, int blue)
  {
    nglSecondaryColor3ui(getContext().glSecondaryColor3ui,
      red,green,blue);
  }
  public static void glSecondaryColor3uiv(
    int[] v)
  {
    nglSecondaryColor3uiv(getContext().glSecondaryColor3uiv,
      v);
  }
  public static void glSecondaryColor3us(
    short red, short green, short blue)
  {
    nglSecondaryColor3us(getContext().glSecondaryColor3us,
      red,green,blue);
  }
  public static void glSecondaryColor3usv(
    short[] v)
  {
    nglSecondaryColor3usv(getContext().glSecondaryColor3usv,
      v);
  }
  public static void glSecondaryColorPointer(
    int size, int type, int stride, Buffer pointer)
  {
    nglSecondaryColorPointer(getContext().glSecondaryColorPointer,
      size,type,stride,pointer);
  }
  public static void glWindowPos2d(
    double x, double y)
  {
    nglWindowPos2d(getContext().glWindowPos2d,
      x,y);
  }
  public static void glWindowPos2dv(
    double[] v)
  {
    nglWindowPos2dv(getContext().glWindowPos2dv,
      v);
  }
  public static void glWindowPos2f(
    float x, float y)
  {
    nglWindowPos2f(getContext().glWindowPos2f,
      x,y);
  }
  public static void glWindowPos2fv(
    float[] v)
  {
    nglWindowPos2fv(getContext().glWindowPos2fv,
      v);
  }
  public static void glWindowPos2i(
    int x, int y)
  {
    nglWindowPos2i(getContext().glWindowPos2i,
      x,y);
  }
  public static void glWindowPos2iv(
    int[] v)
  {
    nglWindowPos2iv(getContext().glWindowPos2iv,
      v);
  }
  public static void glWindowPos2s(
    short x, short y)
  {
    nglWindowPos2s(getContext().glWindowPos2s,
      x,y);
  }
  public static void glWindowPos2sv(
    short[] v)
  {
    nglWindowPos2sv(getContext().glWindowPos2sv,
      v);
  }
  public static void glWindowPos3d(
    double x, double y, double z)
  {
    nglWindowPos3d(getContext().glWindowPos3d,
      x,y,z);
  }
  public static void glWindowPos3dv(
    double[] v)
  {
    nglWindowPos3dv(getContext().glWindowPos3dv,
      v);
  }
  public static void glWindowPos3f(
    float x, float y, float z)
  {
    nglWindowPos3f(getContext().glWindowPos3f,
      x,y,z);
  }
  public static void glWindowPos3fv(
    float[] v)
  {
    nglWindowPos3fv(getContext().glWindowPos3f,
      v);
  }
  public static void glWindowPos3i(
    int x, int y, int z)
  {
    nglWindowPos3i(getContext().glWindowPos3i,
      x,y,z);
  }
  public static void glWindowPos3iv(
    int[] v)
  {
    nglWindowPos3iv(getContext().glWindowPos3iv,
      v);
  }
  public static void glWindowPos3s(
    short x, short y, short z)
  {
    nglWindowPos3s(getContext().glWindowPos3s,
      x,y,z);
  }
  public static void glWindowPos3sv(
    short[] v)
  {
    nglWindowPos3sv(getContext().glWindowPos3sv,
      v);
  }
  private static native void nglBlendFuncSeparate(long pfunc,
    int sfactorRGB, int dfactorRGB, int sfactorAlpha, int dfactorAlpha);
  private static native void nglFogCoordf(long pfunc,
    float coord);
  private static native void nglFogCoordfv(long pfunc,
    float[] coord);
  private static native void nglFogCoordd(long pfunc,
    double coord);
  private static native void nglFogCoorddv(long pfunc,
    double[] coord);
  private static native void nglFogCoordPointer(long pfunc,
    int type, int stride, Buffer pointer);
  private static native void nglMultiDrawArrays(long pfunc,
    int mode, int[] first, int[] count, int primcount);
  private static native void nglMultiDrawElements(long pfunc,
    int mode, int[] count, int type, byte[][] indices, int primcount);
  private static native void nglMultiDrawElements(long pfunc,
    int mode, int[] count, int type, int[][] indices, int primcount);
  private static native void nglMultiDrawElements(long pfunc,
    int mode, int[] count, int type, short[][] indices, int primcount);
  private static native void nglPointParameterf(long pfunc,
    int pname, float param);
  private static native void nglPointParameterfv(long pfunc,
    int pname, float[] params);
  private static native void nglPointParameteri(long pfunc,
    int pname, int param);
  private static native void nglPointParameteriv(long pfunc,
    int pname, int[] params);
  private static native void nglSecondaryColor3b(long pfunc,
    byte red, byte green, byte blue);
  private static native void nglSecondaryColor3bv(long pfunc,
    byte[] v);
  private static native void nglSecondaryColor3d(long pfunc,
    double red, double green, double blue);
  private static native void nglSecondaryColor3dv(long pfunc,
    double[] v);
  private static native void nglSecondaryColor3f(long pfunc,
    float red, float green, float blue);
  private static native void nglSecondaryColor3fv(long pfunc,
    float[] v);
  private static native void nglSecondaryColor3i(long pfunc,
    int red, int green, int blue);
  private static native void nglSecondaryColor3iv(long pfunc,
    int[] v);
  private static native void nglSecondaryColor3s(long pfunc,
    short red, short green, short blue);
  private static native void nglSecondaryColor3sv(long pfunc,
    short[] v);
  private static native void nglSecondaryColor3ub(long pfunc,
    byte red, byte green, byte blue);
  private static native void nglSecondaryColor3ubv(long pfunc,
    byte[] v);
  private static native void nglSecondaryColor3ui(long pfunc,
    int red, int green, int blue);
  private static native void nglSecondaryColor3uiv(long pfunc,
    int[] v);
  private static native void nglSecondaryColor3us(long pfunc,
    short red, short green, short blue);
  private static native void nglSecondaryColor3usv(long pfunc,
    short[] v);
  private static native void nglSecondaryColorPointer(long pfunc,
    int size, int type, int stride, Buffer pointer);
  private static native void nglWindowPos2d(long pfunc,
    double x, double y);
  private static native void nglWindowPos2dv(long pfunc,
    double[] v);
  private static native void nglWindowPos2f(long pfunc,
    float x, float y);
  private static native void nglWindowPos2fv(long pfunc,
    float[] v);
  private static native void nglWindowPos2i(long pfunc,
    int x, int y);
  private static native void nglWindowPos2iv(long pfunc,
    int[] v);
  private static native void nglWindowPos2s(long pfunc,
    short x, short y);
  private static native void nglWindowPos2sv(long pfunc,
    short[] v);
  private static native void nglWindowPos3d(long pfunc,
    double x, double y, double z);
  private static native void nglWindowPos3dv(long pfunc,
    double[] v);
  private static native void nglWindowPos3f(long pfunc,
    float x, float y, float z);
  private static native void nglWindowPos3fv(long pfunc,
    float[] v);
  private static native void nglWindowPos3i(long pfunc,
    int x, int y, int z);
  private static native void nglWindowPos3iv(long pfunc,
    int[] v);
  private static native void nglWindowPos3s(long pfunc,
    short x, short y, short z);
  private static native void nglWindowPos3sv(long pfunc,
    short[] v);

  ///////////////////////////////////////////////////////////////////////////
  // OpenGL 1.5

  public static void glGenQueries(
    int n, int[] ids)
  {
    nglGenQueries(getContext().glGenQueries,
      n,ids);
  }
  public static void glDeleteQueries(
    int n, int[] ids)
  {
    nglDeleteQueries(getContext().glDeleteQueries,
      n,ids);
  }
  public static boolean glIsQuery(
    int id)
  {
    return nglIsQuery(getContext().glIsQuery,
      id);
  }
  public static void glBeginQuery(
    int target, int id)
  {
    nglBeginQuery(getContext().glBeginQuery,
      target,id);
  }
  public static void glEndQuery(
    int target)
  {
    nglEndQuery(getContext().glEndQuery,
      target);
  }
  public static void glGetQueryiv(
    int target, int pname, int[] params)
  {
    nglGetQueryiv(getContext().glGetQueryiv,
      target,pname,params);
  }
  public static void glGetQueryObjectiv(
    int id, int pname, int[] params)
  {
    nglGetQueryObjectiv(getContext().glGetQueryObjectiv,
      id,pname,params);
  }
  public static void glGetQueryObjectuiv(
    int id, int pname, int[] params)
  {
    nglGetQueryObjectuiv(getContext().glGetQueryObjectuiv,
      id,pname,params);
  }
  public static void glBindBuffer(
    int target, int buffer)
  {
    nglBindBuffer(getContext().glBindBuffer,
      target,buffer);
  }
  public static void glDeleteBuffers(
    int n, int[] buffers)
  {
    nglDeleteBuffers(getContext().glDeleteBuffers,
      n,buffers);
  }
  public static void glGenBuffers(
    int n, int[] buffers)
  {
    nglGenBuffers(getContext().glGenBuffers,
      n,buffers);
  }
  public static boolean glIsBuffer(
    int buffer)
  {
    return nglIsBuffer(getContext().glIsBuffer,
      buffer);
  }
  public static void glBufferData(
    int target, int size, Buffer data, int usage)
  {
    nglBufferData(getContext().glBufferData,
      target,size,data,usage);
  }
  public static void glBufferSubData(
    int target, int offset, int size, Buffer data)
  {
    nglBufferSubData(getContext().glBufferSubData,
      target,offset,size,data);
  }
  public static void glGetBufferSubData(
    int target, int offset, int size, Buffer data)
  {
    nglGetBufferSubData(getContext().glGetBufferSubData,
      target,offset,size,data);
  }
  public static ByteBuffer glMapBuffer(
    int target, int access, int size, ByteBuffer buffer)
  {
    return nglMapBuffer(getContext().glMapBuffer,
      target,access,size,buffer);
  }
  public static boolean glUnmapBuffer(
    int target)
  {
    return nglUnmapBuffer(getContext().glUnmapBuffer,
      target);
  }
  public static void glGetBufferParameteriv(
    int target, int pname, int[] params)
  {
    nglGetBufferParameteriv(getContext().glGetBufferParameteriv,
      target,pname,params);
  }
//  public static void glGetBufferPointerv(
//    int target, int pname, Buffer[] params)
//  {
//    nglGetBufferPointerv(getContext().glGetBufferPointerv,
//      target,pname,params);
//  }
  private static native void nglGenQueries(long pfunc,
    int n, int[] ids);
  private static native void nglDeleteQueries(long pfunc,
    int n, int[] ids);
  private static native boolean nglIsQuery(long pfunc,
    int id);
  private static native void nglBeginQuery(long pfunc,
    int target, int id);
  private static native void nglEndQuery(long pfunc,
    int target);
  private static native void nglGetQueryiv(long pfunc,
    int target, int pname, int[] params);
  private static native void nglGetQueryObjectiv(long pfunc,
    int id, int pname, int[] params);
  private static native void nglGetQueryObjectuiv(long pfunc,
    int id, int pname, int[] params);
  private static native void nglBindBuffer(long pfunc,
    int target, int buffer);
  private static native void nglDeleteBuffers(long pfunc,
    int n, int[] buffers);
  private static native void nglGenBuffers(long pfunc,
    int n, int[] buffers);
  private static native boolean nglIsBuffer(long pfunc,
    int buffer);
  private static native void nglBufferData(long pfunc,
    int target, int size, Buffer data, int usage);
  private static native void nglBufferSubData(long pfunc,
    int target, int offset, int size, Buffer data);
  private static native void nglGetBufferSubData(long pfunc,
    int target, int offset, int size, Buffer data);
  private static native ByteBuffer nglMapBuffer(long pfunc,
    int target, int access, int size, ByteBuffer buffer);
  private static native boolean nglUnmapBuffer(long pfunc,
    int target);
  private static native void nglGetBufferParameteriv(long pfunc,
    int target, int pname, int[] params);
//  private static native void nglGetBufferPointerv(long pfunc,
//    int target, int pname, Buffer[] params);

  ///////////////////////////////////////////////////////////////////////////
  // package

  static GlContext getContext() {
    return (GlContext)_context.get();
  }

  static void setContext(GlContext context) {
    _context.set(context);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private static ThreadLocal _context = new ThreadLocal();
  private Gl() {
  }

  static {
    System.loadLibrary("edu_mines_jves_opengl");
  }
}
