// A constant buffer that stores the modelview matrix.
cbuffer ModelViewProjectionConstantBuffer : register(b0)
{
	matrix modelView;
};

// Per-vertex data used as input to the vertex shader.
struct VertexShaderInput
{
	float4 pos : POSITION;
	float4 color : COLOR0;
};

// Per-pixel color data passed through the pixel shader.
struct PixelShaderInput
{
	float4 pos : SV_POSITION;
	float4 color : COLOR0;
};

// Simple shader to do vertex processing on the GPU.
PixelShaderInput main(VertexShaderInput input)
{
	PixelShaderInput output;

	// Transform the vertex position into projected space.
	pos = mul(input.pos, modelView);
	output.pos = pos;

	// Pass the color through without modification.
	output.color = input.color;

	return output;
}
